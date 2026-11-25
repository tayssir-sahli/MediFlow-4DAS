package com.mediflow.dossier.model;

import com.mediflow.dossier.model.enums.Gravite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Allergie {

    private String id;
    private String substance;
    private Gravite gravite;
}