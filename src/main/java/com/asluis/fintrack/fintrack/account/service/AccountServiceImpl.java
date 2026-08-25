package com.asluis.fintrack.fintrack.account.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.account.domain.Account;
import com.asluis.fintrack.fintrack.account.domain.AccountFactory;
import com.asluis.fintrack.fintrack.account.dto.AccountMapper;
import com.asluis.fintrack.fintrack.account.dto.AccountRequest;
import com.asluis.fintrack.fintrack.account.dto.AccountResponse;
import com.asluis.fintrack.fintrack.account.repository.AccountRepository;
import com.asluis.fintrack.fintrack.shared.domain.Currency;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.shared.repository.CurrencyRepository;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountFactory accountFactory;

    @Override
    public AccountResponse create(AccountRequest request) {

        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new EntityNotFoundException(User.class, request.userId()));

        Currency currency = currencyRepository.findById(request.currencyId())
            .orElseThrow(() -> new EntityNotFoundException(Currency.class, request.currencyId()));

        Account accountEntity = AccountMapper.toEntity(request, user, currency, accountFactory);

        return AccountMapper.toResponse(accountRepository.save(accountEntity));
    }

    @Override
    public AccountResponse update(Long id, AccountRequest request) {
        // Every AccountRequest field is either an immutable attribute/relation (owner,
        // currency, type, account number -- no mutators exist for any of them) or governed
        // by dedicated business actions already on the entity (deposit/withdraw/activate/
        // deactivate/freeze/close) that don't fit a generic field-set update.
        throw new UnsupportedOperationException(
            "Account has no generically updatable fields; use its dedicated business actions instead");
    }

    @Override
    public Optional<AccountResponse> findById(Long id) {
        return accountRepository.findById(id).map(AccountMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.delete(findAccount(id));
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Account.class, id));
    }
}