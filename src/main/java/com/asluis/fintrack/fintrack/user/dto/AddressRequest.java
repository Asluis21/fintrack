package com.asluis.fintrack.fintrack.user.dto;

public record AddressRequest(
        String name,
        String street,
        String city,
        String postalCode,
        Long countryId,
        Long userId
) {
}
