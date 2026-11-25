package com.mediflow.dossier.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "Ajout d'une consultation avec son compte rendu")
public record ConsultationRequest(

        @Schema(example = "2026-11-20T10:30:00")
        @NotNull(message = "La date est obligatoire")
        @PastOrPresent(message = "La date ne peut pas être dans le futur")
        LocalDateTime date,

        @Schema(example = "Gastrite")
        @NotBlank(message = "Le diagnostic est obligatoire")
        @Size(max = 300, message = "Le diagnostic ne doit pas dépasser 300 caractères")
        String diagnostic,

        @Schema(example = "Traitement de 7 jours, contrôle dans 2 semaines.")
        @Size(max = 5000, message = "Les notes ne doivent pas dépasser 5000 caractères")
        String notes) {
}