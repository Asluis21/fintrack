package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.domain.PersonalData;
import com.asluis.fintrack.fintrack.user.domain.PersonalDataFactory;
import com.asluis.fintrack.fintrack.user.domain.User;

public class PersonalDataMapper {

    private PersonalDataMapper() {
    }

    public static PersonalData toEntity(PersonalDataRequest request, Country country, User user,
            PersonalDataFactory personalDataFactory) {
        return personalDataFactory.builder(request.documentNumber(), country)
                .name(request.name())
                .surname(request.surname())
                .phone(request.phone())
                .gender(request.gender())
                .birthdate(request.birthdate())
                .user(user)
                .build();
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
