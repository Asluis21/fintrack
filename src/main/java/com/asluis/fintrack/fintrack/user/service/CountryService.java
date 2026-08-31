package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.user.dto.CountryRequest;
import com.asluis.fintrack.fintrack.user.dto.CountryResponse;

public interface CountryService {

    CountryResponse create(CountryRequest request);

    CountryResponse update(Long id, CountryRequest request);

    Optional<CountryResponse> findById(Long id);

    void deleteById(Long id);
}
