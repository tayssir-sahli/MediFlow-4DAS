package com.mediflow.dossier.dto.request;

import com.mediflow.dossier.model.enums.Gravite;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Ajout d'une allergie")
public record AllergieRequest(

        @Schema(example = "Pénicilline")
        @NotBlank(message = "La substance est obligatoire")
        @Size(max = 100, message = "La substance ne doit pas dépasser 100 caractères")
        String substance,

        @Schema(example = "GRAVE")
        @NotNull(message = "La gravité est obligatoire")
        Gravite gravite) {
}