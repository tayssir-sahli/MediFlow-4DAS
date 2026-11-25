package com.mediflow.dossier.repository;

import com.mediflow.dossier.model.DossierMedical;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DossierRepository extends MongoRepository<DossierMedical, String> {

    Optional<DossierMedical> findByPatientId(Long patientId);

    boolean existsByPatientId(Long patientId);

    long deleteByPatientId(Long patientId);
}