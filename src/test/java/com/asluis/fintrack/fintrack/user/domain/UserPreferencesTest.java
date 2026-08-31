package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserPreferencesTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    UserPreferencesTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    private User user() {
        return User.builder()
                .email(new UserEmail("user@example.com", encryptionService, hashService))
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
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
