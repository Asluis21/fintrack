package com.asluis.fintrack.fintrack.transaction.dto;

import com.asluis.fintrack.fintrack.transaction.domain.AccountingType;
import com.asluis.fintrack.fintrack.transaction.domain.CategoryOwnerType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotBlank String name,
        @NotNull AccountingType accountingType,
        @NotNull CategoryOwnerType categoryOwnerType,
        Long createdById
) {
}
