package com.asluis.fintrack.fintrack.shared.security.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.security.domain.SecurityUser;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashService hashService;

    @InjectMocks
    private UserDetailService userDetailService;

    private final EncryptionService encryptionService = mock(EncryptionService.class);

    private User user() {
        return new User("hashedPassword", Role.USER, new UserEmail("user@example.com", encryptionService, mock(HashService.class)));
    }

    @Test
    void loadUserByUsername_trimsEmailBeforeHashing() {
        User user = user();
        when(hashService.hash("user@example.com")).thenReturn("lookup-hash");
        when(userRepository.findByUserEmail_EmailLookupHash("lookup-hash")).thenReturn(Optional.of(user));

        UserDetails result = userDetailService.loadUserByUsername("  user@example.com  ");

        verify(hashService).hash("user@example.com");
        assertThat(((SecurityUser) result).getUser()).isSameAs(user);
    }

    @Test
    void loadUserByUsername_throwsWhenEmailNotFound() {
        when(hashService.hash(anyString())).thenReturn("unknown-hash");
        when(userRepository.findByUserEmail_EmailLookupHash("unknown-hash")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailService.loadUserByUsername("missing@example.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsername_throwsWhenEmailIsNull() {
        assertThatThrownBy(() -> userDetailService.loadUserByUsername(null))
                .isInstanceOf(UsernameNotFoundException.class);

        verifyNoInteractions(hashService, userRepository);
    }

    @Test
    void loadUserByUsername_throwsWhenEmailIsBlank() {
        assertThatThrownBy(() -> userDetailService.loadUserByUsername("   "))
                .isInstanceOf(UsernameNotFoundException.class);

        verifyNoInteractions(hashService, userRepository);
    }
}
