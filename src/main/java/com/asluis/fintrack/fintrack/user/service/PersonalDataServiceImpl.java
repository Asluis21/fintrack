package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.domain.PersonalData;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.dto.PersonalDataMapper;
import com.asluis.fintrack.fintrack.user.dto.PersonalDataRequest;
import com.asluis.fintrack.fintrack.user.dto.PersonalDataResponse;
import com.asluis.fintrack.fintrack.user.repository.CountryRepository;
import com.asluis.fintrack.fintrack.user.repository.PersonalDataRepository;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PersonalDataServiceImpl implements PersonalDataService{

    private final PersonalDataRepository personalDataRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final HashService hashService;

    @Override
    public PersonalDataResponse create(PersonalDataRequest request) {

        Country foundCountry = countryRepository.findById(request.countryId())
            .orElseThrow(() -> new EntityNotFoundException(Country.class, request.countryId()));

        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new EntityNotFoundException(User.class, request.userId()));

        PersonalData personalDataEntity = PersonalDataMapper.toEntity(request, foundCountry, user,
            encryptionService, hashService);

        return PersonalDataMapper.toResponse(personalDataRepository.save(personalDataEntity));
    }

    @Override
    public void deleteById(Long id) {
        
        personalDataRepository.delete(findPersonalData(id));
    }

    @Override
    public Optional<PersonalDataResponse> findById(Long id) {
        
        return personalDataRepository.findById(id).map(PersonalDataMapper::toResponse);
    }

    @Override
    public PersonalDataResponse update(Long id, PersonalDataRequest request) {
        
        PersonalData foundPersonalData = findPersonalData(id);
        
        foundPersonalData.changeBirthdate(request.birthdate());
        foundPersonalData.changeGender(request.gender());
        foundPersonalData.changeName(request.name(), request.surname());
        foundPersonalData.changePhone(request.phone());

        return PersonalDataMapper.toResponse(personalDataRepository.save(foundPersonalData));
    }
    
    private PersonalData findPersonalData(Long id){

        return personalDataRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(PersonalData.class, id));
    }
}
