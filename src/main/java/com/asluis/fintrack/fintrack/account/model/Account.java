package com.asluis.fintrack.fintrack.account.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;

import com.asluis.fintrack.fintrack.user.model.User;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "accounts",
    schema = "account_domain",
    uniqueConstraints = {
        @UniqueConstraint(name="uk_account_account_number_hash", columnNames = "account_number_hash"),
        @UniqueConstraint(name="uk_account_user", columnNames = "user_id")
    },
    check = {
        @CheckConstraint(name="chk_account_balance", constraint = "balance >= 0")
    }
)
public class Account {

    public Account(User user, String accountNumberHash, String accountNumberEncrypted, Currency currency,
            AccountType accountType) {
        this.user = user;
        this.accountNumberHash = accountNumberHash;
        this.accountNumberEncrypted = accountNumberEncrypted;
        this.currency = currency;
        this.accountType = accountType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(name = "fk_account_user"),
        nullable = false
    )
    private User user;

    @Column(length = 128, name = "account_number_hash", nullable = false)
    private String accountNumberHash;

    @Column(length = 500, name = "account_number_encrypted", nullable = false)
    private String accountNumberEncrypted;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ONE;

    @ManyToOne
    @JoinColumn(
        name = "currency_id",
        nullable = false
    )
    private Currency currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "account_type", length = 20, nullable = false)
    private AccountType accountType;

    @Column(nullable = false, length = 10)
    private Status status = Status.ACTIVE;

    protected void setAccountNumberHash(String accountNumberHash) {
        this.accountNumberHash = accountNumberHash;
    }

    protected void setAccountNumberEncrypted(String accountNumberEncrypted) {
        this.accountNumberEncrypted = accountNumberEncrypted;
    }

    protected void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    protected void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    protected void setStatus(Status status) {
        this.status = status;
    }
}
