package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.Address;
import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.domain.User;

public class AddressMapper {

    private AddressMapper() {
    }

    public static Address toEntity(AddressRequest request, Country country, User user) {
        return new Address(request.name(), request.street(), request.city(), request.postalCode(), country, user);
    }

    public static AddressResponse toResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getName(),
                address.getStreet(),
                address.getCity(),
                address.getPostalCode(),
                address.getCountry().getId(),
                address.getUser().getId(),
                address.getCreatedAt()
        );
    }
}
