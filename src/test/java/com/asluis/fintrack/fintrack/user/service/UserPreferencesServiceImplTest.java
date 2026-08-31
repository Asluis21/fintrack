package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.LanguageCode;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.domain.UserPreferences;
import com.asluis.fintrack.fintrack.user.dto.UserPreferencesRequest;
import com.asluis.fintrack.fintrack.user.dto.UserPreferencesResponse;
import com.asluis.fintrack.fintrack.user.repository.UserPreferencesRepository;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPreferencesServiceImplTest {

    @Mock
    private UserPreferencesRepository userPreferencesRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPreferencesServiceImpl userPreferencesService;

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    private User user() {
        return User.builder()
                .email(new UserEmail("user@example.com", encryptionService, hashService))
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void create_userNotFound_throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userPreferencesService.create(new UserPreferencesRequest(true, LanguageCode.EN, 1L)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_savesAndReturnsResponse() {
        User user = user();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userPreferencesRepository.save(any(UserPreferences.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserPreferencesResponse response = userPreferencesService.create(
                new UserPreferencesRequest(true, LanguageCode.EN, 1L));

        assertThat(response.darkMode()).isTrue();
        assertThat(response.languageCode()).isEqualTo(LanguageCode.EN);
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(userPreferencesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(userPreferencesService.findById(1L)).isEmpty();
    }

    @Test
    void update_notFound_throws() {
        when(userPreferencesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userPreferencesService.update(1L, new UserPreferencesRequest(true, LanguageCode.EN, 1L)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_mutatesAndSaves() {
        UserPreferences preferences = new UserPreferences(false, LanguageCode.EN, user());
        when(userPreferencesRepository.findById(1L)).thenReturn(Optional.of(preferences));
        when(userPreferencesRepository.save(preferences)).thenReturn(preferences);

        UserPreferencesResponse response = userPreferencesService.update(1L,
                new UserPreferencesRequest(true, LanguageCode.ES, 1L));

        assertThat(response.darkMode()).isTrue();
        assertThat(response.languageCode()).isEqualTo(LanguageCode.ES);
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(userPreferencesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userPreferencesService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(userPreferencesRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        UserPreferences preferences = new UserPreferences(false, LanguageCode.EN, user());
        when(userPreferencesRepository.findById(1L)).thenReturn(Optional.of(preferences));

        userPreferencesService.deleteById(1L);

        verify(userPreferencesRepository).delete(preferences);
    }
}
