package com.asluis.fintrack.fintrack.user.dto;

import java.time.LocalDate;

import com.asluis.fintrack.fintrack.user.domain.Gender;

public record PersonalDataRequest(
        String name,
        String surname,
        String phone,
        Gender gender,
        String documentNumberHash,
        String documentNumberEncrypted,
        Long countryId,
        LocalDate birthdate,
        Long userId
) {
}
