package com.mediflow.dossier.dto.response;

import com.mediflow.dossier.model.enums.Gravite;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Allergie d'un patient")
public record AllergieResponse(

        @Schema(example = "7c9e6679-7425-40de-944b-e07fc1f90ae7") String id,
        @Schema(example = "Pénicilline") String substance,
        @Schema(example = "GRAVE") Gravite gravite) {
}