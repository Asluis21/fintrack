package com.asluis.fintrack.fintrack.shared.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.dto.CurrencyRequest;
import com.asluis.fintrack.fintrack.shared.dto.CurrencyResponse;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.shared.repository.CurrencyRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Test
    void create_savesAndReturnsResponse() {
        Currency currency = new Currency("US Dollar", "USD", 2);
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);

        CurrencyResponse response = currencyService.create(new CurrencyRequest("US Dollar", "USD", 2));

        assertThat(response.name()).isEqualTo("US Dollar");
        assertThat(response.isoCode()).isEqualTo("USD");
        assertThat(response.decimalPlaces()).isEqualTo(2);
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(currencyService.findById(1L)).isEmpty();
    }

    @Test
    void update_alwaysThrows_currencyIsImmutable() {
        assertThatThrownBy(() -> currencyService.update(1L, new CurrencyRequest("X", "USD", 2)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> currencyService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(currencyRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        Currency currency = new Currency("US Dollar", "USD", 2);
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));

        currencyService.deleteById(1L);

        verify(currencyRepository).delete(currency);
    }
}
