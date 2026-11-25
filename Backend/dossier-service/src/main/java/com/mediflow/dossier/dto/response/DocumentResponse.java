package com.mediflow.dossier.dto.response;

import com.mediflow.dossier.model.enums.TypeDocument;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Document médical rattaché au dossier")
public record DocumentResponse(

        @Schema(example = "9d8c7b6a-5f4e-4d3c-b2a1-0f9e8d7c6b5a") String id,
        @Schema(example = "ANALYSE") TypeDocument type,
        @Schema(example = "analyse-sang-nov-2026.pdf") String nomFichier,
        @Schema(example = "https://stockage.mediflow.tn/docs/analyse-123.pdf") String url,
        @Schema(example = "2026-11-21T09:15:00") LocalDateTime date) {
}