package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.user.dto.PersonalDataRequest;
import com.asluis.fintrack.fintrack.user.dto.PersonalDataResponse;

public interface PersonalDataService {

    PersonalDataResponse create(PersonalDataRequest request);

    PersonalDataResponse update(Long id, PersonalDataRequest request);

    Optional<PersonalDataResponse> findById(Long id);

    void deleteById(Long id);
}
