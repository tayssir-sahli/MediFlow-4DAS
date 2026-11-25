package com.mediflow.dossier.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Antécédent médical d'un patient")
public record AntecedentResponse(

        @Schema(example = "3f2b8c1a-5d4e-4a7b-9c10-1e2f3a4b5c6d")
        String id,
        @Schema(example = "Appendicectomie")
        String description,
        @Schema(example = "2015-03-02")
        LocalDate date) {
}