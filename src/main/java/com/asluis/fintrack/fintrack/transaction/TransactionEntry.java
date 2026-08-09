package com.asluis.fintrack.fintrack.transaction;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import com.asluis.fintrack.fintrack.account.model.Account;
import com.asluis.fintrack.fintrack.common.domain.BaseEntity;
import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeOptional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
public class TransactionEntry extends BaseEntity{
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name        = "transaction_id",
        foreignKey  = @ForeignKey(name="fk_transaction_entry_transaction"),
        nullable    = false,
        updatable   = false
    )
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name        = "acount_id",
        foreignKey  = @ForeignKey(name="fk_transaction_entry_acount"),
        nullable    = false,
        updatable   = false
    )
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(
        name        = "transaction_entry_type",
        length      = 20,
        nullable    = false,
        updatable   = false
    )
    private TransactionEntryType transactionEntryType;

    @Column(
        nullable    = false,
        updatable   = false
    )
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(
        name        = "currency_id",
        nullable    = false,
        updatable   = false,
        foreignKey  = @ForeignKey(name="fk_transaction_entry_acount")
    )
    private Currency currency;

    @Column(
        length      = 100,
        updatable   = false
    )
    private String description;

    public TransactionEntry(Transaction transaction, Account account, TransactionEntryType transactionEntryType,
            BigDecimal amount, Currency currency, String description) {
        this.transaction            = Objects.requireNonNull(transaction, "Transaction is required");
        this.account                = Objects.requireNonNull(account, "Account is required");
        this.transactionEntryType   = Objects.requireNonNull(transactionEntryType, "Transaction entry type is required");
        this.amount                 = Objects.requireNonNull(amount, "Amount is required");
        this.currency               = Objects.requireNonNull(currency, "Currency is required");
        this.description            = normalizeOptional(description);
    }    
}
