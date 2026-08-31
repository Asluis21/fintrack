package com.asluis.fintrack.fintrack.shared.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.shared.dto.CurrencyRequest;
import com.asluis.fintrack.fintrack.shared.dto.CurrencyResponse;

public interface CurrencyService {

    CurrencyResponse create(CurrencyRequest request);

    CurrencyResponse update(Long id, CurrencyRequest request);

    Optional<CurrencyResponse> findById(Long id);

    void deleteById(Long id);
}
