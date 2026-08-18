package com.asluis.fintrack.fintrack.transaction.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.shared.repository.CurrencyRepository;
import com.asluis.fintrack.fintrack.transaction.domain.Category;
import com.asluis.fintrack.fintrack.transaction.domain.Transaction;
import com.asluis.fintrack.fintrack.transaction.dto.TransactionMapper;
import com.asluis.fintrack.fintrack.transaction.dto.TransactionRequest;
import com.asluis.fintrack.fintrack.transaction.dto.TransactionResponse;
import com.asluis.fintrack.fintrack.transaction.repository.CategoryRepository;
import com.asluis.fintrack.fintrack.transaction.repository.TransactionRepository;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public TransactionResponse create(TransactionRequest request) {

        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new EntityNotFoundException(User.class, request.userId()));

        Category category = categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new EntityNotFoundException(Category.class, request.categoryId()));

        Currency sourceCurrency = currencyRepository.findById(request.sourceCurrencyId())
            .orElseThrow(() -> new EntityNotFoundException(Currency.class, request.sourceCurrencyId()));

        Currency targetCurrency = currencyRepository.findById(request.targetCurrencyId())
            .orElseThrow(() -> new EntityNotFoundException(Currency.class, request.targetCurrencyId()));

        Transaction transactionEntity = TransactionMapper.toEntity(request, user, category, sourceCurrency, targetCurrency);

        return TransactionMapper.toResponse(transactionRepository.save(transactionEntity));
    }

    @Override
    public TransactionResponse update(Long id, TransactionRequest request) {

        Transaction foundTransaction = findTransaction(id);

        if (request.status() != foundTransaction.getStatus()) {
            foundTransaction.changeStatus(request.status());
        }
        foundTransaction.changeDescription(request.description());

        return TransactionMapper.toResponse(transactionRepository.save(foundTransaction));
    }

    @Override
    public Optional<TransactionResponse> findById(Long id) {
        return transactionRepository.findById(id).map(TransactionMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.delete(findTransaction(id));
    }

    private Transaction findTransaction(Long id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Transaction.class, id));
    }
}