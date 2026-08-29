package com.asluis.fintrack.fintrack.user.service;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.domain.Gender;
import com.asluis.fintrack.fintrack.user.domain.IdentityDocument;
import com.asluis.fintrack.fintrack.user.domain.PersonalData;
import com.asluis.fintrack.fintrack.user.domain.PersonalDataFactory;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.dto.PersonalDataRequest;
import com.asluis.fintrack.fintrack.user.dto.PersonalDataResponse;
import com.asluis.fintrack.fintrack.user.repository.CountryRepository;
import com.asluis.fintrack.fintrack.user.repository.PersonalDataRepository;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalDataServiceImplTest {

    @Mock
    private PersonalDataRepository personalDataRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonalDataFactory personalDataFactory;

    @InjectMocks
    private PersonalDataServiceImpl personalDataService;

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

    private IdentityDocument identityDocument() {
        return new IdentityDocument("123456789", country(), encryptionService, hashService);
    }

    private PersonalDataRequest request() {
        return new PersonalDataRequest("Jane", "Doe", "555-1234", Gender.FEMALE, "123456789",
                1L, LocalDate.of(1990, 1, 1), 2L);
    }

    @Test
    void create_countryNotFound_throws() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalDataService.create(request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_userNotFound_throws() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country()));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalDataService.create(request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_savesAndReturnsResponse() {
        User user = user();
        IdentityDocument identityDocument = identityDocument();
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(personalDataFactory.builder(anyString(), any(Country.class)))
                .thenReturn(PersonalData.builder().identityDocument(identityDocument));
        when(personalDataRepository.save(any(PersonalData.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PersonalDataResponse response = personalDataService.create(request());

        assertThat(response.name()).isEqualTo("Jane");
        assertThat(response.surname()).isEqualTo("Doe");
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(personalDataRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(personalDataService.findById(1L)).isEmpty();
    }

    @Test
    void update_notFound_throws() {
        when(personalDataRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalDataService.update(1L, request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_mutatesAndSaves() {
        PersonalData personalData = PersonalData.builder()
                .name("Jane").surname("Doe").phone("555-1234").gender(Gender.FEMALE)
                .birthdate(LocalDate.of(1990, 1, 1))
                .identityDocument(identityDocument())
                .user(user())
                .build();
        Country foundCountry = country();
        when(personalDataRepository.findById(1L)).thenReturn(Optional.of(personalData));
        when(countryRepository.findById(1L)).thenReturn(Optional.of(foundCountry));
        when(personalDataRepository.save(personalData)).thenReturn(personalData);

        PersonalDataResponse response = personalDataService.update(1L,
                new PersonalDataRequest("Janet", "Smith", "555-5678", Gender.FEMALE, "987654321",
                        1L, LocalDate.of(1991, 2, 2), 2L));

        assertThat(response.name()).isEqualTo("Janet");
        assertThat(response.surname()).isEqualTo("Smith");
        assertThat(response.phone()).isEqualTo("555-5678");
        verify(personalDataFactory).updateIdentityDocument(personalData, "987654321", foundCountry);
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(personalDataRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalDataService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(personalDataRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        PersonalData personalData = PersonalData.builder()
                .name("Jane").surname("Doe").phone("555-1234").gender(Gender.FEMALE)
                .birthdate(LocalDate.of(1990, 1, 1))
                .identityDocument(identityDocument())
                .user(user())
                .build();
        when(personalDataRepository.findById(1L)).thenReturn(Optional.of(personalData));

        personalDataService.deleteById(1L);

        verify(personalDataRepository).delete(personalData);
    }
}
