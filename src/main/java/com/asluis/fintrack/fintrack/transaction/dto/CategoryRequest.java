package com.asluis.fintrack.fintrack.transaction.dto;

import com.asluis.fintrack.fintrack.transaction.domain.AccountingType;
import com.asluis.fintrack.fintrack.transaction.domain.CategoryOwnerType;

public record CategoryRequest(
        String name,
        AccountingType accountingType,
        CategoryOwnerType categoryOwnerType,
        Long createdById
) {
}
