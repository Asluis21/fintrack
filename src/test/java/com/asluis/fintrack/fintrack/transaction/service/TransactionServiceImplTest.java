package com.asluis.fintrack.fintrack.transaction.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.shared.repository.CurrencyRepository;
import com.asluis.fintrack.fintrack.transaction.domain.AccountingType;
import com.asluis.fintrack.fintrack.transaction.domain.Category;
import com.asluis.fintrack.fintrack.transaction.domain.CategoryOwnerType;
import com.asluis.fintrack.fintrack.transaction.domain.Status;
import com.asluis.fintrack.fintrack.transaction.domain.Transaction;
import com.asluis.fintrack.fintrack.transaction.domain.Type;
import com.asluis.fintrack.fintrack.transaction.dto.TransactionRequest;
import com.asluis.fintrack.fintrack.transaction.dto.TransactionResponse;
import com.asluis.fintrack.fintrack.transaction.repository.CategoryRepository;
import com.asluis.fintrack.fintrack.transaction.repository.TransactionRepository;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    private User user() {
        return User.builder()
                .email(new UserEmail("user@example.com", encryptionService, hashService))
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
    }

    private Category category() {
        return new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null);
    }

    private Currency usd() {
        return new Currency("US Dollar", "USD", 2);
    }

    private TransactionRequest request() {
        return new TransactionRequest("REF-1", "desc", 1L, Type.DEPOSIT, Status.PENDING, 2L,
                BigDecimal.ONE, OffsetDateTime.now(), 3L, 3L);
    }

    @Test
    void create_userNotFound_throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.create(request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void create_savesAndReturnsResponse() {
        Currency usd = usd();
        User user = user();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category()));
        when(currencyRepository.findById(3L)).thenReturn(Optional.of(usd));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionResponse response = transactionService.create(request());

        assertThat(response.referenceNumber()).isEqualTo("REF-1");
        assertThat(response.status()).isEqualTo(Status.PENDING);
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(transactionService.findById(1L)).isEmpty();
    }

    @Test
    void update_notFound_throws() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.update(1L, request()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_sameStatus_doesNotThrow_onlyDescriptionChanges() {
        Currency usd = usd();
        Transaction transaction = new Transaction("REF-1", "old desc", user(), Type.DEPOSIT, Status.PENDING,
                category(), BigDecimal.ONE, OffsetDateTime.now(), usd, usd);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        assertThatCode(() -> transactionService.update(1L,
                new TransactionRequest("REF-1", "new desc", 1L, Type.DEPOSIT, Status.PENDING, 2L,
                        BigDecimal.ONE, OffsetDateTime.now(), 3L, 3L)))
                .doesNotThrowAnyException();

        assertThat(transaction.getDescription()).isEqualTo("new desc");
        assertThat(transaction.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void update_differentStatus_changesStatus() {
        Currency usd = usd();
        Transaction transaction = new Transaction("REF-1", "desc", user(), Type.DEPOSIT, Status.PENDING,
                category(), BigDecimal.ONE, OffsetDateTime.now(), usd, usd);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        TransactionResponse response = transactionService.update(1L,
                new TransactionRequest("REF-1", "desc", 1L, Type.DEPOSIT, Status.COMPLETED, 2L,
                        BigDecimal.ONE, OffsetDateTime.now(), 3L, 3L));

        assertThat(response.status()).isEqualTo(Status.COMPLETED);
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(transactionRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        Currency usd = usd();
        Transaction transaction = new Transaction("REF-1", "desc", user(), Type.DEPOSIT, Status.PENDING,
                category(), BigDecimal.ONE, OffsetDateTime.now(), usd, usd);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteById(1L);

        verify(transactionRepository).delete(transaction);
    }
}
