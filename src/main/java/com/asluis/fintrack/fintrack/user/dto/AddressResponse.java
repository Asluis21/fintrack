package com.asluis.fintrack.fintrack.user.dto;

import java.time.OffsetDateTime;

public record AddressResponse(
        Long id,
        String name,
        String street,
        String city,
        String postalCode,
        Long countryId,
        Long userId,
        OffsetDateTime createdAt
) {
}
