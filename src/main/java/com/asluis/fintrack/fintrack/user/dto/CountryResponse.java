package com.asluis.fintrack.fintrack.user.dto;

import java.time.OffsetDateTime;

public record CountryResponse(
        Long id,
        String name,
        String isoCode,
        OffsetDateTime createdAt
) {
}
