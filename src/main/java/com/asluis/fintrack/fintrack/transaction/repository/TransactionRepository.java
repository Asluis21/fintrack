package com.asluis.fintrack.fintrack.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asluis.fintrack.fintrack.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByUser_IdAndReferenceNumber(Long userId, String referenceNumber);

    List<Transaction> findByUser_Id(Long userId);
}