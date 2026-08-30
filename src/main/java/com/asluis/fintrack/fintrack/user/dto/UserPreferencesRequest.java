package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.LanguageCode;

import jakarta.validation.constraints.NotNull;

public record UserPreferencesRequest(
        boolean darkMode,
        @NotNull LanguageCode languageCode,
        @NotNull Long userId
) {
}
