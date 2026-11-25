package com.mediflow.dossier.service.impl;

import com.mediflow.dossier.dto.request.AllergieRequest;
import com.mediflow.dossier.dto.request.AntecedentRequest;
import com.mediflow.dossier.dto.request.ConsultationRequest;
import com.mediflow.dossier.dto.request.CreerDossierRequest;
import com.mediflow.dossier.dto.request.DocumentRequest;
import com.mediflow.dossier.dto.request.GroupeSanguinRequest;
import com.mediflow.dossier.dto.response.AllergieResponse;
import com.mediflow.dossier.dto.response.AntecedentResponse;
import com.mediflow.dossier.dto.response.ConsultationResponse;
import com.mediflow.dossier.dto.response.DocumentResponse;
import com.mediflow.dossier.dto.response.DossierResponse;
import com.mediflow.dossier.exception.ConflictException;
import com.mediflow.dossier.exception.ResourceNotFoundException;
import com.mediflow.dossier.mapper.DossierMapper;
import com.mediflow.dossier.model.Allergie;
import com.mediflow.dossier.model.Antecedent;
import com.mediflow.dossier.model.Consultation;
import com.mediflow.dossier.model.DocumentMedical;
import com.mediflow.dossier.model.DossierMedical;
import com.mediflow.dossier.repository.DossierRepository;
import com.mediflow.dossier.service.DossierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DossierServiceImpl implements DossierService {

    private final DossierRepository repository;
    private final DossierMapper mapper;

    // ---------- Dossier ----------

    @Override
    public DossierResponse creerDossier(CreerDossierRequest request) {
        if (repository.existsByPatientId(request.patientId())) {
            throw new ConflictException("Un dossier existe déjà pour le patient " + request.patientId());
        }
        DossierMedical dossier = repository.save(mapper.toEntity(request));
        log.info("Dossier {} créé pour le patient {}", dossier.getId(), dossier.getPatientId());
        return mapper.toResponse(dossier);
    }

    @Override
    public DossierResponse getDossier(Long patientId) {
        return mapper.toResponse(trouverDossier(patientId));
    }

    @Override
    public DossierResponse modifierGroupeSanguin(Long patientId, GroupeSanguinRequest request) {
        DossierMedical dossier = trouverDossier(patientId);
        dossier.setGroupeSanguin(request.groupeSanguin());
        return mapper.toResponse(repository.save(dossier));
    }

    @Override
    public void supprimerDossier(Long patientId) {
        long supprimes = repository.deleteByPatientId(patientId);
        if (supprimes == 0) {
            throw dossierIntrouvable(patientId);
        }
        log.info("Dossier du patient {} supprimé", patientId);
    }

    // ---------- Allergies ----------

    @Override
    public List<AllergieResponse> getAllergies(Long patientId) {
        return mapper.toAllergieResponses(trouverDossier(patientId).getAllergies());
    }

    @Override
    public AllergieResponse ajouterAllergie(Long patientId, AllergieRequest request) {
        DossierMedical dossier = trouverDossier(patientId);
        String substance = request.substance().trim();

        boolean existeDeja = dossier.getAllergies().stream()
                .anyMatch(a -> a.getSubstance().equalsIgnoreCase(substance));
        if (existeDeja) {
            throw new ConflictException("L'allergie '" + substance + "' existe déjà dans ce dossier");
        }

        Allergie allergie = mapper.toEntity(request);
        allergie.setId(nouvelId());
        allergie.setSubstance(substance);
        dossier.getAllergies().add(allergie);
        repository.save(dossier);
        return mapper.toResponse(allergie);
    }

    @Override
    public void supprimerAllergie(Long patientId, String allergieId) {
        DossierMedical dossier = trouverDossier(patientId);
        boolean supprimee = dossier.getAllergies().removeIf(a -> allergieId.equals(a.getId()));
        if (!supprimee) {
            throw new ResourceNotFoundException("Allergie introuvable : " + allergieId);
        }
        repository.save(dossier);
    }

    // ---------- Antécédents ----------

    @Override
    public List<AntecedentResponse> getAntecedents(Long patientId) {
        List<Antecedent> tries = trouverDossier(patientId).getAntecedents().stream()
                .sorted(Comparator.comparing(Antecedent::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        return mapper.toAntecedentResponses(tries);
    }

    @Override
    public AntecedentResponse ajouterAntecedent(Long patientId, AntecedentRequest request) {
        DossierMedical dossier = trouverDossier(patientId);
        Antecedent antecedent = mapper.toEntity(request);
        antecedent.setId(nouvelId());
        dossier.getAntecedents().add(antecedent);
        repository.save(dossier);
        return mapper.toResponse(antecedent);
    }

    // ---------- Consultations ----------

    @Override
    public List<ConsultationResponse> getConsultations(Long patientId) {
        List<Consultation> tries = trouverDossier(patientId).getConsultations().stream()
                .sorted(Comparator.comparing(Consultation::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        return mapper.toConsultationResponses(tries);
    }

    @Override
    public ConsultationResponse ajouterConsultation(Long patientId, ConsultationRequest request) {
        DossierMedical dossier = trouverDossier(patientId);
        Consultation consultation = mapper.toEntity(request);
        consultation.setId(nouvelId());
        dossier.getConsultations().add(consultation);
        repository.save(dossier);
        return mapper.toResponse(consultation);
    }

    // ---------- Documents ----------

    @Override
    public List<DocumentResponse> getDocuments(Long patientId) {
        return mapper.toDocumentResponses(trouverDossier(patientId).getDocuments());
    }

    @Override
    public DocumentResponse ajouterDocument(Long patientId, DocumentRequest request) {
        DossierMedical dossier = trouverDossier(patientId);
        DocumentMedical document = mapper.toEntity(request);
        document.setId(nouvelId());
        document.setDate(LocalDateTime.now());
        dossier.getDocuments().add(document);
        repository.save(dossier);
        return mapper.toResponse(document);
    }

    // ---------- Méthodes privées ----------

    private DossierMedical trouverDossier(Long patientId) {
        return repository.findByPatientId(patientId)
                .orElseThrow(() -> dossierIntrouvable(patientId));
    }

    private ResourceNotFoundException dossierIntrouvable(Long patientId) {
        return new ResourceNotFoundException("Aucun dossier pour le patient " + patientId);
    }

    private String nouvelId() {
        return UUID.randomUUID().toString();
    }
}