package com.asluis.fintrack.fintrack.transaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.account.model.Account;
import com.asluis.fintrack.fintrack.account.model.AccountNumber;
import com.asluis.fintrack.fintrack.account.model.AccountType;
import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.user.model.Role;
import com.asluis.fintrack.fintrack.user.model.User;
import com.asluis.fintrack.fintrack.user.model.UserEmail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionEntryTest {

    private User user() {
        return new User(new UserEmail("encrypted", "hash"), "hashedPassword", Role.USER);
    }

    private Currency usd() {
        return new Currency("US Dollar", "USD", 2);
    }

    private Category category() {
        return new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null);
    }

    private Account account() {
        return new Account(user(), usd(), AccountType.CHECKING, new AccountNumber("encrypted", "hash"));
    }

    private Transaction transaction() {
        Currency usd = usd();
        return new Transaction("REF-1", "desc", user(), Type.DEPOSIT, Status.PENDING, category(),
                BigDecimal.ONE, OffsetDateTime.now(), usd, usd);
    }

    @Test
    void validEntry_isCreated() {
        TransactionEntry entry = new TransactionEntry(transaction(), account(), TransactionEntryType.DEBIT,
                new BigDecimal("100.00"), usd(), "groceries");

        assertThat(entry.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(entry.getTransactionEntryType()).isEqualTo(TransactionEntryType.DEBIT);
    }

    @Test
    void nullAmount_throwsWithClearMessage_notARawNullPointerFromSignum() {
        // Regression test: the constructor used to call amount.signum() before
        // null-checking amount, so a null amount raised a raw NullPointerException
        // with no useful message. This asserts the fixed ordering: the null-check
        // (with a descriptive message) now runs first.
        assertThatThrownBy(() -> new TransactionEntry(transaction(), account(), TransactionEntryType.DEBIT,
                null, usd(), "groceries"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Amount");
    }

    @Test
    void zeroAmount_throws() {
        assertThatThrownBy(() -> new TransactionEntry(transaction(), account(), TransactionEntryType.DEBIT,
                BigDecimal.ZERO, usd(), "groceries"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void negativeAmount_throws() {
        assertThatThrownBy(() -> new TransactionEntry(transaction(), account(), TransactionEntryType.DEBIT,
                new BigDecimal("-10"), usd(), "groceries"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullTransaction_throws() {
        assertThatThrownBy(() -> new TransactionEntry(null, account(), TransactionEntryType.DEBIT,
                new BigDecimal("10"), usd(), "groceries"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void blankDescription_isNormalizedToNull() {
        TransactionEntry entry = new TransactionEntry(transaction(), account(), TransactionEntryType.DEBIT,
                new BigDecimal("10"), usd(), "   ");

        assertThat(entry.getDescription()).isNull();
    }
}
