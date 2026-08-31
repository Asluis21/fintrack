package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.Country;

public class CountryMapper {

    private CountryMapper() {
    }

    public static Country toEntity(CountryRequest request) {
        return new Country(request.name(), request.isoCode());
    }

    public static CountryResponse toResponse(Country country) {
        return new CountryResponse(
                country.getId(),
                country.getName(),
                country.getIsoCode(),
                country.getCreatedAt()
        );
    }
}
