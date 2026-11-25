package com.mediflow.dossier.service;

import com.mediflow.dossier.dto.request.AllergieRequest;
import com.mediflow.dossier.dto.request.CreerDossierRequest;
import com.mediflow.dossier.dto.request.DocumentRequest;
import com.mediflow.dossier.dto.response.AllergieResponse;
import com.mediflow.dossier.dto.response.DocumentResponse;
import com.mediflow.dossier.dto.response.DossierResponse;
import com.mediflow.dossier.exception.ConflictException;
import com.mediflow.dossier.exception.ResourceNotFoundException;
import com.mediflow.dossier.mapper.DossierMapper;
import com.mediflow.dossier.mapper.DossierMapperImpl;
import com.mediflow.dossier.model.Allergie;
import com.mediflow.dossier.model.DossierMedical;
import com.mediflow.dossier.model.enums.Gravite;
import com.mediflow.dossier.model.enums.TypeDocument;
import com.mediflow.dossier.repository.DossierRepository;
import com.mediflow.dossier.service.impl.DossierServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DossierServiceImplTest {

    @Mock
    private DossierRepository repository;

    private final DossierMapper mapper = new DossierMapperImpl();

    private DossierServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DossierServiceImpl(repository, mapper);
    }

    // ---------- Dossier ----------

    @Test
    void creerDossier_refuseUnDoublon() {
        when(repository.existsByPatientId(12L)).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> service.creerDossier(new CreerDossierRequest(12L, "O+")));

        verify(repository, never()).save(any());
    }

    @Test
    void creerDossier_enregistreEtRenvoieLeDossier() {
        when(repository.existsByPatientId(12L)).thenReturn(false);
        when(repository.save(any(DossierMedical.class))).thenAnswer(i -> i.getArgument(0));

        DossierResponse response = service.creerDossier(new CreerDossierRequest(12L, "O+"));

        assertEquals(12L, response.patientId());
        assertEquals("O+", response.groupeSanguin());
        assertTrue(response.allergies().isEmpty());
    }

    @Test
    void getDossier_leveUneExceptionSiDossierAbsent() {
        when(repository.findByPatientId(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getDossier(99L));
    }

    @Test
    void supprimerDossier_leveUneExceptionSiAucunDossier() {
        when(repository.deleteByPatientId(99L)).thenReturn(0L);

        assertThrows(ResourceNotFoundException.class, () -> service.supprimerDossier(99L));
    }

    // ---------- Allergies ----------

    @Test
    void ajouterAllergie_ajouteEtGenereUnId() {
        DossierMedical dossier = DossierMedical.builder().patientId(1L).build();
        when(repository.findByPatientId(1L)).thenReturn(Optional.of(dossier));

        AllergieResponse response = service.ajouterAllergie(1L,
                new AllergieRequest("Pénicilline", Gravite.GRAVE));

        assertNotNull(response.id());
        assertEquals("Pénicilline", response.substance());
        assertEquals(Gravite.GRAVE, response.gravite());
        assertEquals(1, dossier.getAllergies().size());
        verify(repository).save(dossier);
    }

    @Test
    void ajouterAllergie_refuseUnDoublonSansTenirCompteDeLaCasse() {
        DossierMedical dossier = DossierMedical.builder().patientId(1L).build();
        dossier.getAllergies().add(Allergie.builder()
                .id("a1").substance("Pénicilline").gravite(Gravite.LEGERE).build());
        when(repository.findByPatientId(1L)).thenReturn(Optional.of(dossier));

        assertThrows(ConflictException.class, () -> service.ajouterAllergie(1L,
                new AllergieRequest("  pénicilline ", Gravite.MODEREE)));

        verify(repository, never()).save(any());
    }

    @Test
    void supprimerAllergie_leveUneExceptionSiIdentifiantInconnu() {
        DossierMedical dossier = DossierMedical.builder().patientId(1L).build();
        when(repository.findByPatientId(1L)).thenReturn(Optional.of(dossier));

        assertThrows(ResourceNotFoundException.class,
                () -> service.supprimerAllergie(1L, "inconnu"));

        verify(repository, never()).save(any());
    }

    // ---------- Documents ----------

    @Test
    void ajouterDocument_fixeLaDateCoteServeur() {
        DossierMedical dossier = DossierMedical.builder().patientId(1L).build();
        when(repository.findByPatientId(1L)).thenReturn(Optional.of(dossier));

        DocumentResponse response = service.ajouterDocument(1L, new DocumentRequest(
                TypeDocument.ANALYSE, "analyse.pdf", "https://stockage.mediflow.tn/docs/analyse.pdf"));

        assertNotNull(response.id());
        assertNotNull(response.date());
        assertEquals(TypeDocument.ANALYSE, response.type());
        assertEquals(1, dossier.getDocuments().size());
    }
}