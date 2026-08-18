package com.asluis.fintrack.fintrack.shared.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.dto.CurrencyMapper;
import com.asluis.fintrack.fintrack.shared.dto.CurrencyRequest;
import com.asluis.fintrack.fintrack.shared.dto.CurrencyResponse;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.shared.repository.CurrencyRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public CurrencyResponse create(CurrencyRequest request) {

        Currency currencyEntity = CurrencyMapper.toEntity(request);

        return CurrencyMapper.toResponse(currencyRepository.save(currencyEntity));
    }

    @Override
    public CurrencyResponse update(Long id, CurrencyRequest request) {
        // name/isoCode/decimalPlaces are all `updatable = false` at the column level and
        // Currency has no mutators at all -- reference/lookup data, immutable by design.
        throw new UnsupportedOperationException("Currency is immutable and cannot be updated");
    }

    @Override
    public Optional<CurrencyResponse> findById(Long id) {
        return currencyRepository.findById(id).map(CurrencyMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        currencyRepository.delete(findCurrency(id));
    }

    private Currency findCurrency(Long id) {
        return currencyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Currency.class, id));
    }
}