package com.asluis.fintrack.fintrack.account.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.account.dto.AccountRequest;
import com.asluis.fintrack.fintrack.account.dto.AccountResponse;

public interface AccountService {

    AccountResponse create(AccountRequest request);

    AccountResponse update(Long id, AccountRequest request);

    Optional<AccountResponse> findById(Long id);

    void deleteById(Long id);
}
