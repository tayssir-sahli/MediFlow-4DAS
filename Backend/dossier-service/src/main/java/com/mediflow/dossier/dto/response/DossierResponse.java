package com.mediflow.dossier.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Dossier médical complet d'un patient")
public record DossierResponse(

        @Schema(example = "6740a1f2c3d4e5f6a7b8c9d0") String id,
        @Schema(example = "12") Long patientId,
        @Schema(example = "O+") String groupeSanguin,
        List<AllergieResponse> allergies,
        List<AntecedentResponse> antecedents,
        List<ConsultationResponse> consultations,
        List<DocumentResponse> documents,
        @Schema(example = "2026-11-21T09:00:00") LocalDateTime dateCreation,
        @Schema(example = "2026-11-21T09:15:00") LocalDateTime dateMiseAJour) {
}