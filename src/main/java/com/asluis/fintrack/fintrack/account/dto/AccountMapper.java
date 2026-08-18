package com.asluis.fintrack.fintrack.account.dto;

import com.asluis.fintrack.fintrack.account.domain.Account;
import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.user.domain.User;

public class AccountMapper {

    private AccountMapper() {
    }

    public static Account toEntity(AccountRequest request, User user, Currency currency,
            EncryptionService encryptionService, HashService hashService) {
        return new Account(user, currency, request.accountType(), request.accountNumber(),
                encryptionService, hashService);
    }

    public static AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getUser().getId(),
                account.getCurrency().getId(),
                account.getAccountType(),
                account.getStatus(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }
}
