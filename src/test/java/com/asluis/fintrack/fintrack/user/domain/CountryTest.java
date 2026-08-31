package com.asluis.fintrack.fintrack.user.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CountryTest {

    @Test
    void validCountry_isCreated() {
        Country country = new Country("United States", "USA");

        assertThat(country.getName()).isEqualTo("United States");
        assertThat(country.getIsoCode()).isEqualTo("USA");
    }

    @Test
    void blankName_throws() {
        assertThatThrownBy(() -> new Country("   ", "USA"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullIsoCode_throws() {
        assertThatThrownBy(() -> new Country("United States", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rename_updatesName() {
        Country country = new Country("United States", "USA");

        country.rename("USA (renamed)");

        assertThat(country.getName()).isEqualTo("USA (renamed)");
    }

    @Test
    void updateIsoCode_updatesIsoCode() {
        Country country = new Country("United States", "USA");

        country.updateIsoCode("US1");

        assertThat(country.getIsoCode()).isEqualTo("US1");
    }
}
