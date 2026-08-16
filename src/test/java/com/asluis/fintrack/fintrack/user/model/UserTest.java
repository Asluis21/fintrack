package com.asluis.fintrack.fintrack.user.model;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {

    private User newUser() {
        return new User(new UserEmail("encrypted", "hash"), "hashedPassword", Role.USER);
    }

    @Test
    void validUser_isCreatedWithDefaults() {
        User user = newUser();

        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void nullUserEmail_throws() {
        assertThatThrownBy(() -> new User(null, "hashedPassword", Role.USER))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankPasswordHash_throws() {
        assertThatThrownBy(() -> new User(new UserEmail("encrypted", "hash"), "   ", Role.USER))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullRole_throws() {
        assertThatThrownBy(() -> new User(new UserEmail("encrypted", "hash"), "hashedPassword", null))
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
        User user = new User(new UserEmail("encrypted", "hash"), "hashedPassword", Role.ADMIN);

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
        EncryptionService encryptionService = mock(EncryptionService.class);
        when(encryptionService.decrypt("encrypted")).thenReturn("user@example.com");

        assertThat(user.decryptEmail(encryptionService)).isEqualTo("user@example.com");
    }

    @Test
    void changePassword_rejectsBlankPasswordTheSameWayConstructorDoes() {
        User user = newUser();

        user.changePassword("newHashedPassword");

        assertThatThrownBy(() -> user.changePassword("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
