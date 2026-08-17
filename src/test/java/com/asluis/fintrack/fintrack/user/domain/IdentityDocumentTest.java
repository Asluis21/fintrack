package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IdentityDocumentTest {

    private Country country() {
        return new Country("United States", "USA");
    }

    @Test
    void validDocument_isCreated() {
        IdentityDocument document = new IdentityDocument("hash", "encrypted", country());

        assertThat(document.getCountry().getIsoCode()).isEqualTo("USA");
    }

    @Test
    void nullCountry_throws() {
        assertThatThrownBy(() -> new IdentityDocument("hash", "encrypted", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankHash_throws() {
        assertThatThrownBy(() -> new IdentityDocument("   ", "encrypted", country()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void decryptDocumentNumber_delegatesToEncryptionService() {
        IdentityDocument document = new IdentityDocument("hash", "encrypted", country());
        EncryptionService encryptionService = mock(EncryptionService.class);
        when(encryptionService.decrypt("encrypted")).thenReturn("123456789");

        assertThat(document.decryptDocumentNumber(encryptionService)).isEqualTo("123456789");
    }

    @Test
    void updateDocumentDetails_withNullArgument_throws() {
        IdentityDocument document = new IdentityDocument("hash", "encrypted", country());

        assertThatThrownBy(() -> document.updateDocumentDetails(null, "newHash", "newEncrypted"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateDocumentDetails_updatesCountry() {
        IdentityDocument document = new IdentityDocument("hash", "encrypted", country());
        Country newCountry = new Country("Canada", "CAN");

        document.updateDocumentDetails(newCountry, "newHash", "newEncrypted");

        assertThat(document.getCountry().getIsoCode()).isEqualTo("CAN");
    }
}
