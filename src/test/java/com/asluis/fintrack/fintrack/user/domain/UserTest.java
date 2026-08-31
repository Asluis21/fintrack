package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    UserTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    private UserEmail userEmail() {
        return new UserEmail("user@example.com", encryptionService, hashService);
    }

    private User newUser() {
        return User.builder()
                .email(userEmail())
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void validUser_isCreatedWithDefaults() {
        User user = newUser();

        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void nullEmail_throws() {
        assertThatThrownBy(() -> User.builder().email(null).passwordHash("hashedPassword").role(Role.USER).build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankPasswordHash_throws() {
        assertThatThrownBy(() -> User.builder().email(userEmail()).passwordHash("   ").role(Role.USER).build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullRole_throws() {
        assertThatThrownBy(() -> User.builder().email(userEmail()).passwordHash("hashedPassword").role(null).build())
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
        User user = User.builder()
                .email(userEmail())
                .passwordHash("hashedPassword")
                .role(Role.ADMIN)
                .build();

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
    void changeEmail_replacesUserEmail() {
        User user = newUser();
        UserEmail newEmail = new UserEmail("new@example.com", encryptionService, hashService);

        user.changeEmail(newEmail);

        when(encryptionService.decrypt("encrypted")).thenReturn("new@example.com");
        assertThat(user.decryptEmail(encryptionService)).isEqualTo("new@example.com");
    }

    @Test
    void changeEmail_null_throws() {
        User user = newUser();

        assertThatThrownBy(() -> user.changeEmail(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void changePassword_rejectsBlankPasswordTheSameWayConstructorDoes() {
        User user = newUser();

        user.changePassword("newHashedPassword");

        assertThatThrownBy(() -> user.changePassword("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
