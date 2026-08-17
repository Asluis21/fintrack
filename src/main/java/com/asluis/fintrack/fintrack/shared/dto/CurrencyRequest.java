package com.asluis.fintrack.fintrack.shared.dto;

public record CurrencyRequest(
        String name,
        String isoCode,
        int decimalPlaces
) {
}
