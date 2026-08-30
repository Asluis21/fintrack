package com.asluis.fintrack.fintrack.transaction.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.asluis.fintrack.fintrack.transaction.domain.Status;
import com.asluis.fintrack.fintrack.transaction.domain.Type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionRequest(
        @NotBlank String referenceNumber,
        String description,
        @NotNull Long userId,
        @NotNull Type type,
        @NotNull Status status,
        @NotNull Long categoryId,
        @NotNull @Positive BigDecimal exchangeRate,
        @NotNull OffsetDateTime exchangeRateTime,
        @NotNull Long sourceCurrencyId,
        @NotNull Long targetCurrencyId
) {
}
