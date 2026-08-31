package com.asluis.fintrack.fintrack.user.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonalDataTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    PersonalDataTest() {
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

    private Country country() {
        return new Country("United States", "USA");
    }

    private IdentityDocument identityDocument() {
        return new IdentityDocument("123456789", country(), encryptionService, hashService);
    }

    private PersonalData newPersonalData() {
        return new PersonalData("Jane", "Doe", "555-1234", Gender.FEMALE,
                LocalDate.of(1990, 1, 1), identityDocument(), user());
    }

    @Test
    void validPersonalData_isCreated() {
        PersonalData personalData = newPersonalData();

        assertThat(personalData.getName()).isEqualTo("Jane");
        assertThat(personalData.getSurname()).isEqualTo("Doe");
        assertThat(personalData.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void nullGender_throws() {
        assertThatThrownBy(() -> new PersonalData("Jane", "Doe", "555-1234", null,
                LocalDate.of(1990, 1, 1), identityDocument(), user()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void nullUser_throws() {
        assertThatThrownBy(() -> new PersonalData("Jane", "Doe", "555-1234", Gender.FEMALE,
                LocalDate.of(1990, 1, 1), identityDocument(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankPhone_isNormalizedToNull() {
        PersonalData personalData = new PersonalData("Jane", "Doe", "   ", Gender.FEMALE,
                LocalDate.of(1990, 1, 1), identityDocument(), user());

        assertThat(personalData.getPhone()).isNull();
    }

    @Test
    void changeName_updatesNameAndSurname() {
        PersonalData personalData = newPersonalData();

        personalData.changeName("Janet", "Smith");

        assertThat(personalData.getName()).isEqualTo("Janet");
        assertThat(personalData.getSurname()).isEqualTo("Smith");
    }

    @Test
    void changeBirthdate_updatesBirthdate() {
        PersonalData personalData = newPersonalData();

        personalData.changeBirthdate(LocalDate.of(1991, 2, 2));

        assertThat(personalData.getBirthdate()).isEqualTo(LocalDate.of(1991, 2, 2));
    }

    @Test
    void decryptDocumentNumber_delegatesToIdentityDocument() {
        PersonalData personalData = newPersonalData();
        when(encryptionService.decrypt("encrypted")).thenReturn("123456789");

        assertThat(personalData.decryptDocumentNumber(encryptionService)).isEqualTo("123456789");
    }
}
