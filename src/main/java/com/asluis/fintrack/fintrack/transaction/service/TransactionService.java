package com.asluis.fintrack.fintrack.transaction.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.transaction.dto.TransactionRequest;
import com.asluis.fintrack.fintrack.transaction.dto.TransactionResponse;

public interface TransactionService {

    TransactionResponse create(TransactionRequest request);

    TransactionResponse update(Long id, TransactionRequest request);

    Optional<TransactionResponse> findById(Long id);

    void deleteById(Long id);
}
