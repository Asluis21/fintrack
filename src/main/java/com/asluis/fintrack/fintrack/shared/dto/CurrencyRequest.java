package com.asluis.fintrack.fintrack.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record CurrencyRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "[A-Z]{3}", message = "ISO code must be exactly 3 uppercase letters") String isoCode,
        @PositiveOrZero int decimalPlaces
) {
}
