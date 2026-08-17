package com.asluis.fintrack.fintrack.user.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonalDataTest {

    private User user() {
        return new User(new UserEmail("encrypted", "hash"), "hashedPassword", Role.USER);
    }

    private IdentityDocument identityDocument() {
        return new IdentityDocument("docHash", "docEncrypted", new Country("United States", "USA"));
    }

    private PersonalData newPersonalData() {
        return new PersonalData("Jane", "Doe", "555-1234", Gender.FEMALE, identityDocument(),
                LocalDate.of(1990, 1, 1), user());
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
                identityDocument(), LocalDate.of(1990, 1, 1), user()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void nullUser_throws() {
        assertThatThrownBy(() -> new PersonalData("Jane", "Doe", "555-1234", Gender.FEMALE,
                identityDocument(), LocalDate.of(1990, 1, 1), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankPhone_isNormalizedToNull() {
        PersonalData personalData = new PersonalData("Jane", "Doe", "   ", Gender.FEMALE,
                identityDocument(), LocalDate.of(1990, 1, 1), user());

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
        EncryptionService encryptionService = mock(EncryptionService.class);
        when(encryptionService.decrypt("docEncrypted")).thenReturn("987654321");

        assertThat(personalData.decryptDocumentNumber(encryptionService)).isEqualTo("987654321");
    }
}
