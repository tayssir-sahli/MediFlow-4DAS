package com.mediflow.dossier.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Consultation avec son compte rendu")
public record ConsultationResponse(

        @Schema(example = "a1b2c3d4-e5f6-4789-a012-3456789abcde") String id,
        @Schema(example = "2026-11-20T10:30:00") LocalDateTime date,
        @Schema(example = "Gastrite") String diagnostic,
        @Schema(example = "Traitement de 7 jours, contrôle dans 2 semaines.") String notes) {
}