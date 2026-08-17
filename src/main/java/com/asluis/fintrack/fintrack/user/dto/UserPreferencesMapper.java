package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserPreferences;

public class UserPreferencesMapper {

    private UserPreferencesMapper() {
    }

    public static UserPreferences toEntity(UserPreferencesRequest request, User user) {
        return new UserPreferences(request.darkMode(), request.languageCode(), user);
    }

    public static UserPreferencesResponse toResponse(UserPreferences userPreferences) {
        return new UserPreferencesResponse(
                userPreferences.getId(),
                userPreferences.isDarkMode(),
                userPreferences.getLanguageCode(),
                userPreferences.getUser().getId(),
                userPreferences.getCreatedAt()
        );
    }
}
