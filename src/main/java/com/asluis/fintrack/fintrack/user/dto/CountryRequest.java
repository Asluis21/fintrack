package com.asluis.fintrack.fintrack.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CountryRequest(
        @NotBlank String name,
        @NotBlank @Size(min = 3, max = 3) String isoCode
) {
}
