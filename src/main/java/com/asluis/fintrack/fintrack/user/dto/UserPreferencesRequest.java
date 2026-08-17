package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.LanguageCode;

public record UserPreferencesRequest(
        boolean darkMode,
        LanguageCode languageCode,
        Long userId
) {
}
