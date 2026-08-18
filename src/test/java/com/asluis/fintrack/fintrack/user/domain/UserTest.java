package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    UserTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    private User newUser() {
        return new User("user@example.com", "hashedPassword", Role.USER, encryptionService, hashService);
    }

    @Test
    void validUser_isCreatedWithDefaults() {
        User user = newUser();

        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void nullEmail_throws() {
        assertThatThrownBy(() -> new User(null, "hashedPassword", Role.USER, encryptionService, hashService))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankPasswordHash_throws() {
        assertThatThrownBy(() -> new User("user@example.com", "   ", Role.USER, encryptionService, hashService))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullRole_throws() {
        assertThatThrownBy(() -> new User("user@example.com", "hashedPassword", null, encryptionService, hashService))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void changeToAdmin_updatesRole() {
        User user = newUser();

        user.changeToAdmin();

        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void changeToUser_updatesRole() {
        User user = new User("user@example.com", "hashedPassword", Role.ADMIN, encryptionService, hashService);

        user.changeToUser();

        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void suspendUser_updatesStatus() {
        User user = newUser();

        user.suspendUser();

        assertThat(user.getStatus()).isEqualTo(Status.SUSPENDED);
    }

    @Test
    void inactivateUser_updatesStatus() {
        User user = newUser();

        user.inactivateUser();

        assertThat(user.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    void activateUser_updatesStatus() {
        User user = newUser();
        user.suspendUser();

        user.activateUser();

        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void decryptEmail_delegatesToUserEmail() {
        User user = newUser();
        when(encryptionService.decrypt("encrypted")).thenReturn("user@example.com");

        assertThat(user.decryptEmail(encryptionService)).isEqualTo("user@example.com");
    }

    @Test
    void changeEmail_reEncryptsAndReHashesWithNewValue() {
        User user = newUser();

        user.changeEmail("new@example.com", encryptionService, hashService);

        verify(encryptionService).encrypt("new@example.com");
        verify(hashService).hash("new@example.com");
    }

    @Test
    void changePassword_rejectsBlankPasswordTheSameWayConstructorDoes() {
        User user = newUser();

        user.changePassword("newHashedPassword");

        assertThatThrownBy(() -> user.changePassword("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}