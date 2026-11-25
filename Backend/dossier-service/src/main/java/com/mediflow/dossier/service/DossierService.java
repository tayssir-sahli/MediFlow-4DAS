package com.mediflow.dossier.service;

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

import java.util.List;

/**
 * Contrat du microservice Dossier Médical.
 * Toutes les méthodes lèvent ResourceNotFoundException si le patient n'a pas de dossier,
 * sauf creerDossier.
 */
public interface DossierService {

    // ---------- Dossier ----------

    /** Crée le dossier d'un patient. Lève ConflictException s'il existe déjà. */
    DossierResponse creerDossier(CreerDossierRequest request);

    /** Retourne le dossier complet d'un patient. */
    DossierResponse getDossier(Long patientId);

    /** Modifie le groupe sanguin du patient. */
    DossierResponse modifierGroupeSanguin(Long patientId, GroupeSanguinRequest request);

    /** Supprime définitivement le dossier d'un patient. */
    void supprimerDossier(Long patientId);

    // ---------- Allergies ----------

    List<AllergieResponse> getAllergies(Long patientId);

    /** Ajoute une allergie. Lève ConflictException si la substance est déjà enregistrée. */
    AllergieResponse ajouterAllergie(Long patientId, AllergieRequest request);

    /** Supprime une allergie. Lève ResourceNotFoundException si l'identifiant est inconnu. */
    void supprimerAllergie(Long patientId, String allergieId);

    // ---------- Antécédents ----------

    List<AntecedentResponse> getAntecedents(Long patientId);

    AntecedentResponse ajouterAntecedent(Long patientId, AntecedentRequest request);

    // ---------- Consultations ----------

    List<ConsultationResponse> getConsultations(Long patientId);

    ConsultationResponse ajouterConsultation(Long patientId, ConsultationRequest request);

    // ---------- Documents ----------

    List<DocumentResponse> getDocuments(Long patientId);

    DocumentResponse ajouterDocument(Long patientId, DocumentRequest request);
}