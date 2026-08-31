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

class UserEmailTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    UserEmailTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    @Test
    void nullEmail_throws() {
        assertThatThrownBy(() -> new UserEmail(null, encryptionService, hashService))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankEmail_throws() {
        assertThatThrownBy(() -> new UserEmail("   ", encryptionService, hashService))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void construction_encryptsAndHashesTheEmail() {
        new UserEmail("user@example.com", encryptionService, hashService);

        verify(encryptionService).encrypt("user@example.com");
        verify(hashService).hash("user@example.com");
    }

    @Test
    void changeEmail_reEncryptsAndReHashesWithNewValue() {
        UserEmail email = new UserEmail("old@example.com", encryptionService, hashService);

        email.changeEmail("new@example.com", encryptionService, hashService);

        verify(encryptionService).encrypt("new@example.com");
        verify(hashService).hash("new@example.com");
    }

    @Test
    void decryptEmail_delegatesToEncryptionService() {
        UserEmail email = new UserEmail("user@example.com", encryptionService, hashService);
        when(encryptionService.decrypt("encrypted")).thenReturn("user@example.com");

        assertThat(email.decryptEmail(encryptionService)).isEqualTo("user@example.com");
    }
}