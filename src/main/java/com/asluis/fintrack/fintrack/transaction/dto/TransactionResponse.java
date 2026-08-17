package com.asluis.fintrack.fintrack.transaction.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.asluis.fintrack.fintrack.transaction.domain.Status;
import com.asluis.fintrack.fintrack.transaction.domain.Type;

public record TransactionResponse(
        Long id,
        String referenceNumber,
        String description,
        Long userId,
        Type type,
        Status status,
        Long categoryId,
        BigDecimal exchangeRate,
        OffsetDateTime exchangeRateTime,
        Long sourceCurrencyId,
        Long targetCurrencyId,
        OffsetDateTime createdAt
) {
}
