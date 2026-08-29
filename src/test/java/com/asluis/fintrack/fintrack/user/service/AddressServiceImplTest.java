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
import com.asluis.fintrack.fintrack.user.domain.Address;
import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.dto.AddressRequest;
import com.asluis.fintrack.fintrack.user.dto.AddressResponse;
import com.asluis.fintrack.fintrack.user.repository.AddressRepository;
import com.asluis.fintrack.fintrack.user.repository.CountryRepository;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    private Country country() {
        return new Country("United States", "USA");
    }

    private User user() {
        return User.builder()
                .email(new UserEmail("user@example.com", encryptionService, hashService))
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
    }

    private AddressRequest request() {
        return new AddressRequest("Home", "123 Main St", "Springfield", "12345", 1L, 2L);
    }

    @Test
    void create_countryNotFound_throws() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.create(request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_userNotFound_throws() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country()));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.create(request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_savesAndReturnsResponse() {
        User user = user();
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddressResponse response = addressService.create(request());

        assertThat(response.name()).isEqualTo("Home");
        assertThat(response.city()).isEqualTo("Springfield");
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(addressService.findById(1L)).isEmpty();
    }

    @Test
    void update_notFound_throws() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.update(1L, request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_mutatesAndSaves() {
        Address address = new Address("Home", "123 Main St", "Springfield", "12345", country(), user());
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);

        AddressResponse response = addressService.update(1L,
                new AddressRequest("Work", "456 Side St", "Shelbyville", "54321", 1L, 2L));

        assertThat(response.name()).isEqualTo("Work");
        assertThat(response.city()).isEqualTo("Shelbyville");
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(addressRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        Address address = new Address("Home", "123 Main St", "Springfield", "12345", country(), user());
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        addressService.deleteById(1L);

        verify(addressRepository).delete(address);
    }
}
