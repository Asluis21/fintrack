package com.asluis.fintrack.fintrack.user.dto;

import java.time.LocalDate;

import com.asluis.fintrack.fintrack.user.domain.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record PersonalDataRequest(
        @NotBlank String name,
        @NotBlank String surname,
        String phone,
        @NotNull Gender gender,
        @NotBlank String documentNumber,
        @NotNull Long countryId,
        @NotNull @Past LocalDate birthdate,
        @NotNull Long userId
) {
}
