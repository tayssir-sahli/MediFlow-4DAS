package com.mediflow.dossier.dto.request;

import com.mediflow.dossier.model.enums.TypeDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Schema(description = "Ajout d'un document médical")
public record DocumentRequest(

        @Schema(example = "ANALYSE")
        @NotNull(message = "Le type de document est obligatoire")
        TypeDocument type,

        @Schema(example = "analyse-sang-nov-2026.pdf")
        @NotBlank(message = "Le nom du fichier est obligatoire")
        @Size(max = 255, message = "Le nom du fichier ne doit pas dépasser 255 caractères")
        String nomFichier,

        @Schema(example = "https://stockage.mediflow.tn/docs/analyse-123.pdf")
        @NotBlank(message = "L'URL est obligatoire")
        @URL(message = "L'URL du document est invalide")
        @Size(max = 2048, message = "L'URL ne doit pas dépasser 2048 caractères")
        String url) {
}