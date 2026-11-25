package com.mediflow.dossier.dto.request;

import com.mediflow.dossier.validator.GroupeSanguin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Modification du groupe sanguin")
public record GroupeSanguinRequest(

        @Schema(example = "A-")
        @NotBlank(message = "Le groupe sanguin est obligatoire")
        @GroupeSanguin
        String groupeSanguin) {
}