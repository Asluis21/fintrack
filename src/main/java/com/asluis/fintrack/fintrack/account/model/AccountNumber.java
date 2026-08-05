package com.asluis.fintrack.fintrack.account.model;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.validateRequired;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AccountNumber {
    
    @Column(
        name = "account_number_encrypted",
        length = 500, 
        nullable = false, 
        updatable = false
    )
    private String encrypted;

    @Column(
        name = "account_number_hash",
        length = 128, 
        nullable = false, 
        updatable = false
    )
    private String hash;

    public AccountNumber(String encrypted, String hash) {
        this.encrypted = validateRequired(encrypted, "Encrypted");
        this.hash = validateRequired(hash, "Hash");
    }

    protected String encrypted(){
        return encrypted;
    }

    protected String hash(){
        return hash;
    }
}
