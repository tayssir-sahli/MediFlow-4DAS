package com.mediflow.dossier.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class GroupeSanguinValidator implements ConstraintValidator<GroupeSanguin, String> {

    private static final Set<String> VALEURS_ACCEPTEES =
            Set.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

    @Override
    public boolean isValid(String valeur, ConstraintValidatorContext context) {
        return valeur == null || VALEURS_ACCEPTEES.contains(valeur);
    }
}