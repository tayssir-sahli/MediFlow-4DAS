package com.mediflow.dossier.controller;

import com.mediflow.dossier.service.DossierService;
import com.mediflow.dossier.dto.request.CreerDossierRequest;
import com.mediflow.dossier.dto.response.AllergieResponse;
import com.mediflow.dossier.dto.response.DossierResponse;
import com.mediflow.dossier.exception.ConflictException;
import com.mediflow.dossier.exception.ResourceNotFoundException;
import com.mediflow.dossier.model.enums.Gravite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DossierController.class)
class DossierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DossierService service;

    private static DossierResponse dossierExemple() {
        return new DossierResponse("6740a1f2c3d4e5f6a7b8c9d0", 12L, "O+",
                List.of(), List.of(), List.of(), List.of(),
                LocalDateTime.of(2026, 11, 21, 9, 0), LocalDateTime.of(2026, 11, 21, 9, 15));
    }

    // ---------- POST /dossiers ----------

    @Test
    void creerDossier_renvoie201AvecLeHeaderLocation() throws Exception {
        when(service.creerDossier(any(CreerDossierRequest.class))).thenReturn(dossierExemple());

        mockMvc.perform(post("/dossiers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "patientId": 12, "groupeSanguin": "O+" }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/dossiers/12"))
                .andExpect(jsonPath("$.patientId").value(12))
                .andExpect(jsonPath("$.groupeSanguin").value("O+"));
    }

    @Test
    void creerDossier_renvoie400SiGroupeSanguinInvalide() throws Exception {
        mockMvc.perform(post("/dossiers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "patientId": 13, "groupeSanguin": "Z+" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details[0]", startsWith("groupeSanguin")));

        verifyNoInteractions(service);
    }

    @Test
    void creerDossier_renvoie400SiPatientIdManquant() throws Exception {
        mockMvc.perform(post("/dossiers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "groupeSanguin": "O+" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]", startsWith("patientId")));

        verifyNoInteractions(service);
    }

    @Test
    void creerDossier_renvoie409SiLeDossierExisteDeja() throws Exception {
        when(service.creerDossier(any(CreerDossierRequest.class)))
                .thenThrow(new ConflictException("Un dossier existe deja pour le patient 12"));

        mockMvc.perform(post("/dossiers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "patientId": 12, "groupeSanguin": "O+" }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    // ---------- GET /dossiers/{patientId} ----------

    @Test
    void getDossier_renvoie200() throws Exception {
        when(service.getDossier(12L)).thenReturn(dossierExemple());

        mockMvc.perform(get("/dossiers/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(12))
                .andExpect(jsonPath("$.allergies").isArray());
    }

    @Test
    void getDossier_renvoie404SiAucunDossier() throws Exception {
        when(service.getDossier(999L))
                .thenThrow(new ResourceNotFoundException("Aucun dossier pour le patient 999"));

        mockMvc.perform(get("/dossiers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/dossiers/999"));
    }

    @Test
    void getDossier_renvoie400SiLIdentifiantNEstPasUnNombre() throws Exception {
        mockMvc.perform(get("/dossiers/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("patientId")));

        verifyNoInteractions(service);
    }

    // ---------- Allergies ----------

    @Test
    void ajouterAllergie_renvoie201() throws Exception {
        when(service.ajouterAllergie(any(Long.class), any()))
                .thenReturn(new AllergieResponse("a1b2c3", "Pollen", Gravite.MODEREE));

        mockMvc.perform(post("/dossiers/12/allergies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "substance": "Pollen", "gravite": "MODEREE" }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("a1b2c3"))
                .andExpect(jsonPath("$.gravite").value("MODEREE"));
    }

    @Test
    void ajouterAllergie_renvoie400SiLaGraviteEstInconnue() throws Exception {
        mockMvc.perform(post("/dossiers/12/allergies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "substance": "Pollen", "gravite": "TRES_GRAVE" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("illisible")));

        verifyNoInteractions(service);
    }

    @Test
    void ajouterAllergie_renvoie400SiLaSubstanceEstVide() throws Exception {
        mockMvc.perform(post("/dossiers/12/allergies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "substance": "   ", "gravite": "LEGERE" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]", startsWith("substance")));

        verifyNoInteractions(service);
    }

    @Test
    void supprimerAllergie_renvoie204() throws Exception {
        mockMvc.perform(delete("/dossiers/12/allergies/abc"))
                .andExpect(status().isNoContent());

        verify(service).supprimerAllergie(12L, "abc");
    }

    // ---------- Consultations ----------

    @Test
    void ajouterConsultation_renvoie400SiLaDateEstDansLeFutur() throws Exception {
        mockMvc.perform(post("/dossiers/12/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "date": "2999-01-01T10:00:00", "diagnostic": "Grippe", "notes": "Repos" }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]", startsWith("date")));

        verifyNoInteractions(service);
    }

    // ---------- Gestion globale des erreurs ----------

    @Test
    void methodeNonPrevue_renvoie405EtPasUn500() throws Exception {
        mockMvc.perform(put("/dossiers/12"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405));
    }

    @Test
    void erreurInattendue_renvoie500SansReveleLaCause() throws Exception {
        when(service.getDossier(5L))
                .thenThrow(new RuntimeException("connexion MongoDB refusee, mot de passe xyz"));

        mockMvc.perform(get("/dossiers/5"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message", not(containsString("MongoDB"))));
    }
}