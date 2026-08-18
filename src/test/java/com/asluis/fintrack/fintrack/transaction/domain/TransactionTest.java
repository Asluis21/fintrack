package com.asluis.fintrack.fintrack.transaction.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    TransactionTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    private User user() {
        return new User("user@example.com", "hashedPassword", Role.USER, encryptionService, hashService);
    }

    private Category category() {
        return new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null);
    }

    private Currency usd() {
        return new Currency("US Dollar", "USD", 2);
    }

    private Currency eur() {
        return new Currency("Euro", "EUR", 2);
    }

    @Test
    void sameCurrency_withRateOfOne_isValid() {
        Currency usd = usd();

        assertThatCode(() -> new Transaction("REF-1", "desc", user(), Type.DEPOSIT, Status.PENDING, category(),
                BigDecimal.ONE, OffsetDateTime.now(), usd, usd))
                .doesNotThrowAnyException();
    }

    @Test
    void sameCurrency_withRateNotOne_throws() {
        Currency usd = usd();

        assertThatThrownBy(() -> new Transaction("REF-1", "desc", user(), Type.DEPOSIT, Status.PENDING, category(),
                new BigDecimal("1.5"), OffsetDateTime.now(), usd, usd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Exchange rate must be 1");
    }

    @Test
    void differentCurrency_withPositiveRate_isValid() {
        assertThatCode(() -> new Transaction("REF-1", "desc", user(), Type.TRANSFER, Status.PENDING, category(),
                new BigDecimal("0.92"), OffsetDateTime.now(), usd(), eur()))
                .doesNotThrowAnyException();
    }

    @Test
    void differentCurrency_withZeroOrNegativeRate_throws() {
        assertThatThrownBy(() -> new Transaction("REF-1", "desc", user(), Type.TRANSFER, Status.PENDING, category(),
                BigDecimal.ZERO, OffsetDateTime.now(), usd(), eur()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Exchange rate must be greater than 0");
    }

    @Test
    void nullReferenceNumber_throws() {
        Currency usd = usd();

        assertThatThrownBy(() -> new Transaction(null, "desc", user(), Type.DEPOSIT, Status.PENDING, category(),
                BigDecimal.ONE, OffsetDateTime.now(), usd, usd))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void categoryNotAvailableForUser_throws() {
        User owner = user();
        User otherUser = user();
        Category userCategory = new Category("Personal", AccountingType.EXPENSE, CategoryOwnerType.USER, owner);
        Currency usd = usd();

        assertThatThrownBy(() -> new Transaction("REF-1", "desc", otherUser, Type.DEPOSIT, Status.PENDING,
                userCategory, BigDecimal.ONE, OffsetDateTime.now(), usd, usd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category cannot be used by this user");
    }
}
