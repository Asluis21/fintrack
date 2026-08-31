package com.asluis.fintrack.fintrack.user.dto;

import java.time.OffsetDateTime;

import com.asluis.fintrack.fintrack.user.domain.LanguageCode;

public record UserPreferencesResponse(
        Long id,
        boolean darkMode,
        LanguageCode languageCode,
        Long userId,
        OffsetDateTime createdAt
) {
}
