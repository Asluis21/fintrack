package com.asluis.fintrack.fintrack.shared.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyTest {

    @Test
    void validCurrency_isCreated() {
        Currency currency = new Currency("US Dollar", "USD", 2);

        assertThat(currency.getName()).isEqualTo("US Dollar");
        assertThat(currency.getIsoCode()).isEqualTo("USD");
        assertThat(currency.getDecimalPlaces()).isEqualTo(2);
    }

    @Test
    void name_collapsesInternalWhitespace() {
        Currency currency = new Currency("US   Dollar", "USD", 2);

        assertThat(currency.getName()).isEqualTo("US Dollar");
    }

    @Test
    void nullName_throws() {
        assertThatThrownBy(() -> new Currency(null, "USD", 2))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void isoCodeNotThreeUppercaseLetters_throws() {
        assertThatThrownBy(() -> new Currency("US Dollar", "US", 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ISO code");
    }

    @Test
    void isoCodeInLowercase_currentlyThrowsEvenThoughItGetsUppercased() {
        // NOTE: this documents existing behavior, not necessarily intended behavior.
        // The constructor uppercases isoCode into `this.isoCode`, but the regex
        // validation that follows checks the ORIGINAL (still-lowercase) constructor
        // parameter, not the uppercased field -- so a valid lowercase code like "usd"
        // is rejected even though the field would have been set correctly.
        assertThatThrownBy(() -> new Currency("US Dollar", "usd", 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ISO code");
    }

    @Test
    void negativeDecimalPlaces_throws() {
        assertThatThrownBy(() -> new Currency("US Dollar", "USD", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Decimal places");
    }
}
