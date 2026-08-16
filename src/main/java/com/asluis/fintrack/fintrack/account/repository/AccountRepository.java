package com.asluis.fintrack.fintrack.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asluis.fintrack.fintrack.account.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUser_Id(Long userId);

    Optional<Account> findByAccountNumber_AccountNumberHash(String accountNumberHash);
}