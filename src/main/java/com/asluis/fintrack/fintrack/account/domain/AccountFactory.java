package com.asluis.fintrack.fintrack.account.domain;

import org.springframework.stereotype.Component;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AccountFactory {
    
    private final EncryptionService encryptionService;
    private final HashService hashService;

    public Account.AccountBuilder builder(String accountNumberInput){
        AccountNumber accountNumber = new AccountNumber(accountNumberInput, encryptionService, hashService);

        return Account.builder()
            .accountNumber(accountNumber);
    }
}
