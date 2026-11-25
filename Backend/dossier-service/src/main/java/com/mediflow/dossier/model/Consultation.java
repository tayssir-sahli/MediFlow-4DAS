package com.mediflow.dossier.model;

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
public class Consultation {

    private String id;
    private LocalDateTime date;
    private String diagnostic;
    private String notes;
}