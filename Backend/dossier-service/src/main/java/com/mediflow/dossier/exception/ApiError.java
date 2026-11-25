package com.mediflow.dossier.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Format uniforme des erreurs de l'API")
public record ApiError(

        @Schema(example = "2026-11-21T09:15:00") LocalDateTime timestamp,
        @Schema(example = "404") int status,
        @Schema(example = "Not Found") String error,
        @Schema(example = "Aucun dossier pour le patient 12") String message,
        @Schema(example = "/dossiers/12") String path,
        @Schema(description = "Détail des champs invalides (erreurs 400 uniquement)") List<String> details) {
}