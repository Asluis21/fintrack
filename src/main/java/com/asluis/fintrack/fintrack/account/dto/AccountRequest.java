package com.asluis.fintrack.fintrack.account.dto;

import com.asluis.fintrack.fintrack.account.domain.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
        @NotNull Long userId,
        @NotNull Long currencyId,
        @NotNull AccountType accountType,
        @NotBlank String accountNumber
) {
}
