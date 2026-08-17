package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserEmailTest {

    @Test
    void nullEncryptedEmail_throws() {
        assertThatThrownBy(() -> new UserEmail(null, "hash"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankLookupHash_throws() {
        assertThatThrownBy(() -> new UserEmail("encrypted", "   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmail_updatesEncryptedValueUsedForDecryption() {
        UserEmail email = new UserEmail("encrypted", "hash");
        email.changeEmail("newEncrypted", "newHash");

        EncryptionService encryptionService = mock(EncryptionService.class);
        when(encryptionService.decrypt("newEncrypted")).thenReturn("new@example.com");

        assertThat(email.decryptEmail(encryptionService)).isEqualTo("new@example.com");
    }

    @Test
    void decryptEmail_delegatesToEncryptionService() {
        UserEmail email = new UserEmail("encrypted", "hash");
        EncryptionService encryptionService = mock(EncryptionService.class);
        when(encryptionService.decrypt("encrypted")).thenReturn("user@example.com");

        assertThat(email.decryptEmail(encryptionService)).isEqualTo("user@example.com");
    }
}
