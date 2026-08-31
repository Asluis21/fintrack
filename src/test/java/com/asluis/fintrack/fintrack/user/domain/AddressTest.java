package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddressTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    AddressTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    private Country country() {
        return new Country("United States", "USA");
    }

    private User user() {
        return User.builder()
                .email(new UserEmail("user@example.com", encryptionService, hashService))
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
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
