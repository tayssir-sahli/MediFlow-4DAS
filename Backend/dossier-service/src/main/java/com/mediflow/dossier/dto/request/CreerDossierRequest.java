package com.mediflow.dossier.dto.request;

import com.mediflow.dossier.validator.GroupeSanguin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Création du dossier médical d'un patient")
public record CreerDossierRequest(

        @Schema(example = "12")
        @NotNull(message = "L'identifiant du patient est obligatoire")
        @Positive(message = "L'identifiant du patient doit être positif")
        Long patientId,

        @Schema(example = "O+")
        @GroupeSanguin
        String groupeSanguin) {
}