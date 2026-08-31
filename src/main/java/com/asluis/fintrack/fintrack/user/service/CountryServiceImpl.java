package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.dto.CountryMapper;
import com.asluis.fintrack.fintrack.user.dto.CountryRequest;
import com.asluis.fintrack.fintrack.user.dto.CountryResponse;
import com.asluis.fintrack.fintrack.user.repository.CountryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CountryServiceImpl implements CountryService{

    private final CountryRepository countryRepository;

    @Transactional
    @Override
    public CountryResponse create(CountryRequest request) {
        
        Country countryEntity = CountryMapper.toEntity(request);
        return CountryMapper.toResponse(countryRepository.save(countryEntity));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        
        countryRepository.delete(findCountry(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CountryResponse> findById(Long id) {
        
        return countryRepository.findById(id).map(CountryMapper::toResponse);
    }

    @Transactional
    @Override
    public CountryResponse update(Long id, CountryRequest request) {
        
        Country foundCountry = findCountry(id);

        foundCountry.rename(request.name());
        foundCountry.updateIsoCode(request.isoCode());
 
        return CountryMapper.toResponse(countryRepository.save(foundCountry));
    }
    
    private Country findCountry (Long id){

        return countryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Country.class, id));
    }
}
