package com.asluis.fintrack.fintrack.shared.dto;

import com.asluis.fintrack.fintrack.shared.domain.Currency;

public class CurrencyMapper {

    private CurrencyMapper() {
    }

    public static Currency toEntity(CurrencyRequest request) {
        return new Currency(request.name(), request.isoCode(), request.decimalPlaces());
    }

    public static CurrencyResponse toResponse(Currency currency) {
        return new CurrencyResponse(
                currency.getId(),
                currency.getName(),
                currency.getIsoCode(),
                currency.getDecimalPlaces(),
                currency.getCreatedAt()
        );
    }
}
