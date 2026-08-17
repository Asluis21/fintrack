package com.asluis.fintrack.fintrack.account.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.asluis.fintrack.fintrack.account.domain.AccountType;
import com.asluis.fintrack.fintrack.account.domain.Status;

public record AccountResponse(
        Long id,
        Long userId,
        Long currencyId,
        AccountType accountType,
        Status status,
        BigDecimal balance,
        OffsetDateTime createdAt
) {
}
