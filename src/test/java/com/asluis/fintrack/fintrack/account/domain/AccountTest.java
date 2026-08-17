package com.asluis.fintrack.fintrack.account.domain;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountTest {

    private User user() {
        return new User(new UserEmail("encrypted", "hash"), "hashedPassword", Role.USER);
    }

    private Currency currency() {
        return new Currency("US Dollar", "USD", 2);
    }

    private AccountNumber accountNumber() {
        return new AccountNumber("encrypted", "hash");
    }

    private Account newAccount() {
        return new Account(user(), currency(), AccountType.CHECKING, accountNumber());
    }

    @Test
    void validAccount_isCreatedWithZeroBalanceAndActiveStatus() {
        Account account = newAccount();

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(account.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void nullUser_throws() {
        assertThatThrownBy(() -> new Account(null, currency(), AccountType.CHECKING, accountNumber()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void deposit_increasesBalance() {
        Account account = newAccount();

        account.deposit(new BigDecimal("100.00"));

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void deposit_negativeAmount_throws() {
        Account account = newAccount();

        assertThatThrownBy(() -> account.deposit(new BigDecimal("-1")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withdraw_reducesBalance() {
        Account account = newAccount();
        account.deposit(new BigDecimal("100.00"));

        account.withdraw(new BigDecimal("40.00"));

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("60.00"));
    }

    @Test
    void withdraw_moreThanBalance_throws() {
        Account account = newAccount();
        account.deposit(new BigDecimal("50.00"));

        assertThatThrownBy(() -> account.withdraw(new BigDecimal("100.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void freezeThenActivate_changesStatus() {
        Account account = newAccount();

        account.freeze();
        assertThat(account.getStatus()).isEqualTo(Status.FROZEN);

        account.activate();
        assertThat(account.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void settingSameStatusAgain_throws() {
        Account account = newAccount();

        assertThatThrownBy(account::activate)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void closedAccount_rejectsAnyFurtherStatusChange() {
        Account account = newAccount();
        account.close();

        assertThatThrownBy(account::activate).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(account::freeze).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(account::close).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void decryptAccountNumber_delegatesToAccountNumber() {
        Account account = newAccount();
        EncryptionService encryptionService = mock(EncryptionService.class);
        when(encryptionService.decrypt("encrypted")).thenReturn("000123456789");

        assertThat(account.decryptAccountNumber(encryptionService)).isEqualTo("000123456789");
    }
}
