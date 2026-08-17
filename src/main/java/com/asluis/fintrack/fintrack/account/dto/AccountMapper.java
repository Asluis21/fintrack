package com.asluis.fintrack.fintrack.account.dto;

import com.asluis.fintrack.fintrack.account.domain.Account;
import com.asluis.fintrack.fintrack.account.domain.AccountNumber;
import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.user.domain.User;

public class AccountMapper {

    private AccountMapper() {
    }

    public static Account toEntity(AccountRequest request, User user, Currency currency) {
        AccountNumber accountNumber = new AccountNumber(request.accountNumberEncrypted(), request.accountNumberHash());
        return new Account(user, currency, request.accountType(), accountNumber);
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
