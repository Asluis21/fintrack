package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.domain.PersonalData;
import com.asluis.fintrack.fintrack.user.domain.User;

public class PersonalDataMapper {

    private PersonalDataMapper() {
    }

    public static PersonalData toEntity(PersonalDataRequest request, Country country, User user,
            EncryptionService encryptionService, HashService hashService) {
        return new PersonalData(request.name(), request.surname(), request.phone(), request.gender(),
                request.documentNumber(), country, request.birthdate(), user, encryptionService, hashService);
    }

    public static PersonalDataResponse toResponse(PersonalData personalData) {
        return new PersonalDataResponse(
                personalData.getId(),
                personalData.getName(),
                personalData.getSurname(),
                personalData.getPhone(),
                personalData.getGender(),
                personalData.getIdentityDocument().getCountry().getId(),
                personalData.getBirthdate(),
                personalData.getUser().getId(),
                personalData.getCreatedAt()
        );
    }
}
