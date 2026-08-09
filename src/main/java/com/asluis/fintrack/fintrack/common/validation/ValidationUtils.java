package com.asluis.fintrack.fintrack.common.validation;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {
    
    public static String validateRequired(String value, String field) {
        value = Objects.requireNonNull(value, field + " must not be null").trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }

        return value;
    } 

    public static String normalizeOptional(String value) {
        if(value == null) return null;
    
        value = value.trim();

        return value.isBlank() ? null : value;
    }

    public static String normalizeRequiredText(String name, String field) {
        return validateRequired(name, field)
                .replaceAll("\\s+", " ");
    }
}
