package com.asluis.fintrack.fintrack.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    private Country country() {
        return new Country("United States", "USA");
    }

    private User user() {
        return new User(new UserEmail("encrypted", "hash"), "hashedPassword", Role.USER);
    }

    @Test
    void validAddress_isCreated() {
        Address address = new Address("Home", "123 Main St", "Springfield", "12345", country(), user());

        assertThat(address.getName()).isEqualTo("Home");
        assertThat(address.getStreet()).isEqualTo("123 Main St");
        assertThat(address.getCity()).isEqualTo("Springfield");
        assertThat(address.getPostalCode()).isEqualTo("12345");
    }

    @Test
    void blankPostalCode_isNormalizedToNull() {
        Address address = new Address("Home", "123 Main St", "Springfield", "   ", country(), user());

        assertThat(address.getPostalCode()).isNull();
    }

    @Test
    void nullCountry_throws() {
        assertThatThrownBy(() -> new Address("Home", "123 Main St", "Springfield", "12345", null, user()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankStreet_throws() {
        assertThatThrownBy(() -> new Address("Home", "   ", "Springfield", "12345", country(), user()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeCity_updatesCity() {
        Address address = new Address("Home", "123 Main St", "Springfield", "12345", country(), user());

        address.changeCity("Shelbyville");

        assertThat(address.getCity()).isEqualTo("Shelbyville");
    }
}
