package com.mediflow.dossier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "dossiers_medicaux")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DossierMedical {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long patientId;

    private String groupeSanguin;

    @Builder.Default
    private List<Allergie> allergies = new ArrayList<>();

    @Builder.Default
    private List<Antecedent> antecedents = new ArrayList<>();

    @Builder.Default
    private List<Consultation> consultations = new ArrayList<>();

    @Builder.Default
    private List<DocumentMedical> documents = new ArrayList<>();

    @CreatedDate
    private LocalDateTime dateCreation;

    @LastModifiedDate
    private LocalDateTime dateMiseAJour;

    @Version
    private Long version;
}