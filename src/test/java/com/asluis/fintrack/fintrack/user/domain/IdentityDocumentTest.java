package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IdentityDocumentTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    IdentityDocumentTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    private Country country() {
        return new Country("United States", "USA");
    }

    @Test
    void validDocument_isCreated() {
        IdentityDocument document = new IdentityDocument("123456789", country(), encryptionService, hashService);

        assertThat(document.getCountry().getIsoCode()).isEqualTo("USA");
    }

    @Test
    void nullCountry_throws() {
        assertThatThrownBy(() -> new IdentityDocument("123456789", null, encryptionService, hashService))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankDocumentNumber_throws() {
        assertThatThrownBy(() -> new IdentityDocument("   ", country(), encryptionService, hashService))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void decryptDocumentNumber_delegatesToEncryptionService() {
        IdentityDocument document = new IdentityDocument("123456789", country(), encryptionService, hashService);
        when(encryptionService.decrypt("encrypted")).thenReturn("123456789");

        assertThat(document.decryptDocumentNumber(encryptionService)).isEqualTo("123456789");
    }

    @Test
    void updateDocumentDetails_nullCountry_throws() {
        IdentityDocument document = new IdentityDocument("123456789", country(), encryptionService, hashService);

        assertThatThrownBy(() -> document.updateDocumentDetails(null, "987654321", encryptionService, hashService))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void updateDocumentDetails_updatesCountryAndReEncrypts() {
        IdentityDocument document = new IdentityDocument("123456789", country(), encryptionService, hashService);
        Country newCountry = new Country("Canada", "CAN");

        document.updateDocumentDetails(newCountry, "987654321", encryptionService, hashService);

        assertThat(document.getCountry().getIsoCode()).isEqualTo("CAN");
        verify(encryptionService).encrypt("987654321");
        verify(hashService).hash("987654321");
    }
}