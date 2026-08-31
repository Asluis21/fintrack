package com.asluis.fintrack.fintrack.user.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.asluis.fintrack.fintrack.user.domain.Gender;

public record PersonalDataResponse(
        Long id,
        String name,
        String surname,
        String phone,
        Gender gender,
        Long countryId,
        LocalDate birthdate,
        Long userId,
        OffsetDateTime createdAt
) {
}
