package com.mediflow.dossier.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Ajout d'un antécédent médical")
public record AntecedentRequest(

        @Schema(example = "Appendicectomie")
        @NotBlank(message = "La description est obligatoire")
        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
        String description,

        @Schema(example = "2015-03-02")
        @NotNull(message = "La date est obligatoire")
        @PastOrPresent(message = "La date ne peut pas être dans le futur")
        LocalDate date) {
}