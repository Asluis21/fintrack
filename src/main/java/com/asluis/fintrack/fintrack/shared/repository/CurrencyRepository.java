package com.asluis.fintrack.fintrack.shared.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asluis.fintrack.fintrack.shared.domain.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByIsoCode(String isoCode);
}