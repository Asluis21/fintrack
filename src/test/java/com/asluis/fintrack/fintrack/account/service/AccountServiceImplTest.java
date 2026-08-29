package com.asluis.fintrack.fintrack.account.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.asluis.fintrack.fintrack.account.domain.Account;
import com.asluis.fintrack.fintrack.account.domain.AccountFactory;
import com.asluis.fintrack.fintrack.account.domain.AccountNumber;
import com.asluis.fintrack.fintrack.account.domain.AccountType;
import com.asluis.fintrack.fintrack.account.dto.AccountRequest;
import com.asluis.fintrack.fintrack.account.dto.AccountResponse;
import com.asluis.fintrack.fintrack.account.repository.AccountRepository;
import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.shared.repository.CurrencyRepository;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private AccountFactory accountFactory;

    @InjectMocks
    private AccountServiceImpl accountService;

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    private User user() {
        return User.builder()
                .email(new UserEmail("user@example.com", encryptionService, hashService))
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
    }

    private Currency currency() {
        return new Currency("US Dollar", "USD", 2);
    }

    private AccountNumber accountNumber() {
        return new AccountNumber("000123456789", encryptionService, hashService);
    }

    private AccountRequest request() {
        return new AccountRequest(1L, 2L, AccountType.CHECKING, "000123456789");
    }

    @Test
    void create_userNotFound_throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.create(request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_currencyNotFound_throws() {
        User user = user();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(currencyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.create(request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_savesAndReturnsResponse() {
        User user = user();
        AccountNumber accountNumber = accountNumber();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(currencyRepository.findById(2L)).thenReturn(Optional.of(currency()));
        when(accountFactory.builder("000123456789")).thenReturn(Account.builder().accountNumber(accountNumber));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountResponse response = accountService.create(request());

        assertThat(response.accountType()).isEqualTo(AccountType.CHECKING);
    }

    @Test
    void update_alwaysThrows_noGenericallyUpdatableFields() {
        assertThatThrownBy(() -> accountService.update(1L, request()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(accountService.findById(1L)).isEmpty();
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(accountRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        Account account = Account.builder()
                .user(user())
                .currency(currency())
                .accountType(AccountType.CHECKING)
                .accountNumber(accountNumber())
                .build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deleteById(1L);

        verify(accountRepository).delete(account);
    }
}
