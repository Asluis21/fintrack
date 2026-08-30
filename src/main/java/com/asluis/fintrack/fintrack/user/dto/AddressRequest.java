package com.asluis.fintrack.fintrack.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotBlank String name,
        @NotBlank String street,
        @NotBlank String city,
        String postalCode,
        @NotNull Long countryId,
        @NotNull Long userId
) {
}
