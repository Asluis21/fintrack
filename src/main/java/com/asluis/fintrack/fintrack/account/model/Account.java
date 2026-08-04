package com.asluis.fintrack.fintrack.account.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.asluis.fintrack.fintrack.shared.models.Currency;
import com.asluis.fintrack.fintrack.user.model.User;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
        @UniqueConstraint(name="uk_account_account_number_hash", columnNames = "account_number_hash")
    },
    check = {
        @CheckConstraint(name="chk_account_balance", constraint = "balance >= 0")
    }
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(name = "fk_account_user"),
        nullable = false,
        updatable = false
    )
    private User user;

    @Getter(AccessLevel.NONE)
    @Column(length = 128, name = "account_number_hash", nullable = false, updatable = false)
    private String accountNumberHash;

    @Getter(AccessLevel.NONE)
    @Column(length = 500, name = "account_number_encrypted", nullable = false, updatable = false)
    private String accountNumberEncrypted;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(
        precision = 19,
        scale = 4,
        nullable = false
    )
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "currency_id",
        nullable = false,
        foreignKey = @ForeignKey(name="fk_account_currency"),
        updatable = false
    )
    private Currency currency;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", length = 20, nullable = false, updatable = false)
    private AccountType accountType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public Account(User user, String accountNumberHash, String accountNumberEncrypted, Currency currency,
            AccountType accountType) {
        
        this.user                   = Objects.requireNonNull(user, "User must not be null");
        this.accountNumberHash      = Objects.requireNonNull(accountNumberHash, "Account number hash must not be null");
        
        if(this.accountNumberHash.isBlank()){
            throw new IllegalArgumentException("Account number hash must not be blank");
        }
        
        this.accountNumberEncrypted = Objects.requireNonNull(accountNumberEncrypted, "Account number encrypted must not be null");

        if(this.accountNumberEncrypted.isBlank()){
            throw new IllegalArgumentException("Account number encrypted must not be null");
        }

        this.currency               = Objects.requireNonNull(currency, "Currency must not be null");
        this.accountType            = Objects.requireNonNull(accountType, "Account type must not be null");
    }

    @PrePersist
    void prePersist(){

        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    protected void deposit(BigDecimal amount) {

        this.validateAmount(amount);

        this.balance = balance.add(amount);
    }

    protected void withdraw(BigDecimal amount){

        this.validateAmount(amount);

        if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        balance = balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount){
        amount = Objects.requireNonNull(amount, "Amount must not be null");

        if(amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
    }

    public String decryptAccountNumber(){
        return null;
    }

    protected void activate(){
        changeStatus(Status.ACTIVE);
    }

    protected void deactivate(){
        changeStatus(Status.INACTIVE);
    }

    protected void freeze(){
        changeStatus(Status.FROZEN);
    }

    protected void close(){
        changeStatus(Status.CLOSED);
    }

    private void changeStatus(Status newStatus){

         if(status == Status.CLOSED){
            throw new IllegalStateException("Closed account cannot change status");
        }


        if(this.status == newStatus){
            throw new IllegalArgumentException("Account is already " + newStatus);
        }

        this.status = newStatus;
    }
}
