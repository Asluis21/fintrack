package com.asluis.fintrack.fintrack.user.domain;

import org.springframework.stereotype.Component;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PersonalDataFactory {

    private final EncryptionService encryptionService;
    private final HashService hashService;

    public PersonalData.PersonalDataBuilder builder(String documentNumber, Country country) {
        IdentityDocument identityDocument = new IdentityDocument(documentNumber, country, encryptionService, hashService);

        return PersonalData.builder()
                .identityDocument(identityDocument);
    }
}
