package com.asluis.fintrack.fintrack.account.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountNumberTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    AccountNumberTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    @Test
    void blankAccountNumber_throws() {
        assertThatThrownBy(() -> new AccountNumber("   ", encryptionService, hashService))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullAccountNumber_throws() {
        assertThatThrownBy(() -> new AccountNumber(null, encryptionService, hashService))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void decryptAccountNumber_delegatesToEncryptionService() {
        AccountNumber accountNumber = new AccountNumber("000123456789", encryptionService, hashService);
        when(encryptionService.decrypt("encrypted")).thenReturn("000123456789");

        assertThat(accountNumber.decryptAccountNumber(encryptionService)).isEqualTo("000123456789");
    }
}