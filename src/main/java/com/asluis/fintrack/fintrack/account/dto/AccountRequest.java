package com.asluis.fintrack.fintrack.account.dto;

import com.asluis.fintrack.fintrack.account.domain.AccountType;

public record AccountRequest(
        Long userId,
        Long currencyId,
        AccountType accountType,
        String accountNumberEncrypted,
        String accountNumberHash
) {
}
