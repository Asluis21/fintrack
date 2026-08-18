package com.asluis.fintrack.fintrack.account.domain;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.validateRequired;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AccountNumber {

    @Column(
        name        = "account_number_encrypted",
        length      = 500,
        nullable    = false,
        updatable   = false
    )
    private String accountNumberEncrypted;

    @Column(
        name        = "account_number_hash",
        length      = 128,
        nullable    = false,
        updatable   = false
    )
    private String accountNumberHash;

    public AccountNumber(String accountNumber, EncryptionService encryptionService, HashService hashService) {
        String normalizedAccountNumber = validateRequired(accountNumber, "Account number");
        this.accountNumberEncrypted    = encryptionService.encrypt(normalizedAccountNumber);
        this.accountNumberHash         = hashService.hash(normalizedAccountNumber);
    }

    protected String decryptAccountNumber(EncryptionService encryptionService){
        return encryptionService.decrypt(accountNumberEncrypted);
    }
}
