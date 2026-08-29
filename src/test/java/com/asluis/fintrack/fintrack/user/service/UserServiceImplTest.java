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
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.domain.UserFactory;
import com.asluis.fintrack.fintrack.user.dto.UserRequest;
import com.asluis.fintrack.fintrack.user.dto.UserResponse;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFactory userFactory;

    @InjectMocks
    private UserServiceImpl userService;

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    private UserEmail userEmail() {
        return new UserEmail("user@example.com", encryptionService, hashService);
    }

    @Test
    void create_savesAndReturnsResponse() {
        UserEmail userEmail = userEmail();
        when(userFactory.builder("user@example.com", "plainPassword"))
                .thenReturn(User.builder().email(userEmail).passwordHash("hashedPassword"));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.create(new UserRequest("user@example.com", "plainPassword", Role.USER));

        assertThat(response.role()).isEqualTo(Role.USER);
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(userService.findById(1L)).isEmpty();
    }

    @Test
    void update_notFound_throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(1L, new UserRequest("user@example.com", "newPassword", Role.USER)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_changesEmailPasswordAndRole() {
        User user = User.builder().email(userEmail()).passwordHash("hashedPassword").role(Role.USER).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userFactory.hashPassword("newPassword")).thenReturn("newHashedPassword");
        when(userRepository.save(user)).thenReturn(user);

        UserResponse response = userService.update(1L, new UserRequest("new@example.com", "newPassword", Role.ADMIN));

        assertThat(response.role()).isEqualTo(Role.ADMIN);
        verify(userFactory).changeEmail(user, "new@example.com");
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        User user = User.builder().email(userEmail()).passwordHash("hashedPassword").role(Role.USER).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userRepository).delete(user);
    }
}
