package com.asluis.fintrack.fintrack.transaction.dto;

import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.transaction.domain.Category;
import com.asluis.fintrack.fintrack.transaction.domain.Transaction;
import com.asluis.fintrack.fintrack.user.domain.User;

public class TransactionMapper {

    private TransactionMapper() {
    }

    public static Transaction toEntity(TransactionRequest request, User user, Category category,
            Currency sourceCurrency, Currency targetCurrency) {
        return new Transaction(request.referenceNumber(), request.description(), user, request.type(),
                request.status(), category, request.exchangeRate(), request.exchangeRateTime(),
                sourceCurrency, targetCurrency);
    }

    public static TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getReferenceNumber(),
                transaction.getDescription(),
                transaction.getUser().getId(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getCategory().getId(),
                transaction.getExchangeRate(),
                transaction.getExchangeRateTime(),
                transaction.getSourceCurrency().getId(),
                transaction.getTargetCurrency().getId(),
                transaction.getCreatedAt()
        );
    }
}
