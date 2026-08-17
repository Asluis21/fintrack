package com.asluis.fintrack.fintrack.account.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountNumberTest {

    @Test
    void blankEncrypted_throws() {
        assertThatThrownBy(() -> new AccountNumber("   ", "hash"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullHash_throws() {
        assertThatThrownBy(() -> new AccountNumber("encrypted", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void decryptAccountNumber_delegatesToEncryptionService() {
        AccountNumber accountNumber = new AccountNumber("encrypted", "hash");
        EncryptionService encryptionService = mock(EncryptionService.class);
        when(encryptionService.decrypt("encrypted")).thenReturn("000123456789");

        assertThat(accountNumber.decryptAccountNumber(encryptionService)).isEqualTo("000123456789");
    }
}
