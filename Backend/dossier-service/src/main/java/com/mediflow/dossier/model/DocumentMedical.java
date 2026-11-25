package com.mediflow.dossier.model;

import com.mediflow.dossier.model.enums.TypeDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMedical {

    private String id;
    private TypeDocument type;
    private String nomFichier;
    private String url;
    private LocalDateTime date;
}