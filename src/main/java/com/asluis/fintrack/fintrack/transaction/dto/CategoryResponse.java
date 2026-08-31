package com.asluis.fintrack.fintrack.transaction.dto;

import java.time.OffsetDateTime;

import com.asluis.fintrack.fintrack.transaction.domain.AccountingType;
import com.asluis.fintrack.fintrack.transaction.domain.CategoryOwnerType;

public record CategoryResponse(
        Long id,
        String name,
        AccountingType accountingType,
        CategoryOwnerType categoryOwnerType,
        Long createdById,
        OffsetDateTime createdAt
) {
}
