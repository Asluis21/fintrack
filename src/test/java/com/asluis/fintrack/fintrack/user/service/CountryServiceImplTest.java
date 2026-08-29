package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.dto.CountryRequest;
import com.asluis.fintrack.fintrack.user.dto.CountryResponse;
import com.asluis.fintrack.fintrack.user.repository.CountryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    @Test
    void create_savesAndReturnsResponse() {
        Country country = new Country("United States", "USA");
        when(countryRepository.save(any(Country.class))).thenReturn(country);

        CountryResponse response = countryService.create(new CountryRequest("United States", "USA"));

        assertThat(response.name()).isEqualTo("United States");
        assertThat(response.isoCode()).isEqualTo("USA");
        verify(countryRepository).save(any(Country.class));
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(countryService.findById(1L)).isEmpty();
    }

    @Test
    void findById_found_returnsResponse() {
        Country country = new Country("United States", "USA");
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        assertThat(countryService.findById(1L)).isPresent();
    }

    @Test
    void update_notFound_throws() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.update(1L, new CountryRequest("X", "USA")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_mutatesAndSaves() {
        Country country = new Country("United States", "USA");
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(countryRepository.save(country)).thenReturn(country);

        CountryResponse response = countryService.update(1L, new CountryRequest("USA (renamed)", "USA"));

        assertThat(response.name()).isEqualTo("USA (renamed)");
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(countryRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        Country country = new Country("United States", "USA");
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        countryService.deleteById(1L);

        verify(countryRepository).delete(country);
    }
}
