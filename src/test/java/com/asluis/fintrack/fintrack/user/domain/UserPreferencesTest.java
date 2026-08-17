package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserPreferencesTest {

    private User user() {
        return new User(new UserEmail("encrypted", "hash"), "hashedPassword", Role.USER);
    }

    @Test
    void validPreferences_areCreated() {
        UserPreferences preferences = new UserPreferences(true, LanguageCode.EN, user());

        assertThat(preferences.isDarkMode()).isTrue();
        assertThat(preferences.getLanguageCode()).isEqualTo(LanguageCode.EN);
    }

    @Test
    void nullLanguageCode_throws() {
        assertThatThrownBy(() -> new UserPreferences(true, null, user()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void nullUser_throws() {
        assertThatThrownBy(() -> new UserPreferences(true, LanguageCode.EN, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void changeToDarkMode_updatesFlag() {
        UserPreferences preferences = new UserPreferences(false, LanguageCode.EN, user());

        preferences.changeToDarkMode();

        assertThat(preferences.isDarkMode()).isTrue();
    }

    @Test
    void changeToLightMode_updatesFlag() {
        UserPreferences preferences = new UserPreferences(true, LanguageCode.EN, user());

        preferences.changeToLightMode();

        assertThat(preferences.isDarkMode()).isFalse();
    }

    @Test
    void changeLanguageCode_updatesLanguage() {
        UserPreferences preferences = new UserPreferences(true, LanguageCode.EN, user());

        preferences.changeLanguageCode(LanguageCode.ES);

        assertThat(preferences.getLanguageCode()).isEqualTo(LanguageCode.ES);
    }
}
