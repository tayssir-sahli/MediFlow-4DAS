package com.mediflow.dossier.mapper;

import com.mediflow.dossier.dto.request.AllergieRequest;
import com.mediflow.dossier.dto.request.AntecedentRequest;
import com.mediflow.dossier.dto.request.ConsultationRequest;
import com.mediflow.dossier.dto.request.CreerDossierRequest;
import com.mediflow.dossier.dto.request.DocumentRequest;
import com.mediflow.dossier.dto.response.AllergieResponse;
import com.mediflow.dossier.dto.response.AntecedentResponse;
import com.mediflow.dossier.dto.response.ConsultationResponse;
import com.mediflow.dossier.dto.response.DocumentResponse;
import com.mediflow.dossier.dto.response.DossierResponse;
import com.mediflow.dossier.model.Allergie;
import com.mediflow.dossier.model.Antecedent;
import com.mediflow.dossier.model.Consultation;
import com.mediflow.dossier.model.DocumentMedical;
import com.mediflow.dossier.model.DossierMedical;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DossierMapper {

    // ---------- Entité -> Réponse ----------

    DossierResponse toResponse(DossierMedical dossier);

    AllergieResponse toResponse(Allergie allergie);

    AntecedentResponse toResponse(Antecedent antecedent);

    ConsultationResponse toResponse(Consultation consultation);

    DocumentResponse toResponse(DocumentMedical document);

    List<AllergieResponse> toAllergieResponses(List<Allergie> allergies);

    List<AntecedentResponse> toAntecedentResponses(List<Antecedent> antecedents);

    List<ConsultationResponse> toConsultationResponses(List<Consultation> consultations);

    List<DocumentResponse> toDocumentResponses(List<DocumentMedical> documents);

    // ---------- Requête -> Entité ----------

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "patientId", source = "patientId")
    @Mapping(target = "groupeSanguin", source = "groupeSanguin")
    DossierMedical toEntity(CreerDossierRequest request);

    @Mapping(target = "id", ignore = true)
    Allergie toEntity(AllergieRequest request);

    @Mapping(target = "id", ignore = true)
    Antecedent toEntity(AntecedentRequest request);

    @Mapping(target = "id", ignore = true)
    Consultation toEntity(ConsultationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    DocumentMedical toEntity(DocumentRequest request);
}