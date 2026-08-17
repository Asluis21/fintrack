package com.asluis.fintrack.fintrack.shared.dto;

import java.time.OffsetDateTime;

public record CurrencyResponse(
        Long id,
        String name,
        String isoCode,
        int decimalPlaces,
        OffsetDateTime createdAt
) {
}
