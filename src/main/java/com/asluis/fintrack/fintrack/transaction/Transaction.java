package com.asluis.fintrack.fintrack.transaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.asluis.fintrack.fintrack.common.models.Currency;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
    name = "transactions",
    schema = "transaction_domain",
    uniqueConstraints = {
        @UniqueConstraint(
            name="uk_transactions_reference_number", 
            columnNames = {
                "user_id",
                "reference_number"
            })
    },
    check = {
        @CheckConstraint(
            name="chk_transaction_exchange_rate_consistency", 
            constraint = """
            (
                source_currency_id = target_currency_id AND exchange_rate = 1
            )
            OR
            (
                source_currency_id <> target_currency_id AND exchange_rate > 0
            )
            """
        )
    }
)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "reference_number", length = 100, nullable = false, updatable = false)
    private String referenceNumber;

    @Column(length = 200)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name="user_id",
        foreignKey = @ForeignKey(name = "fk_transactions_user"),
        nullable = false, 
        updatable = false
    )
    private User user;

    @Column(length = 20, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    
    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "category_id",
        foreignKey = @ForeignKey(name="fk_transactions_category"), 
        nullable = false,
        updatable = false
    )    
    private Category category;

    @Column(
        name = "exchange_rate",
        precision = 19,
        scale=8, 
        nullable = false, 
        updatable = false
    )
    private BigDecimal exchangeRate;
    
    @Column(name = "exchange_rate_time", nullable = false, updatable = false)
    private OffsetDateTime exchangeRateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "source_currency_id",
        foreignKey = @ForeignKey(name = "fk_transactions_source_currency"),  
        nullable = false, 
        updatable = false
    )
    private Currency sourceCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "target_currency_id", 
        foreignKey = @ForeignKey(name="fk_transactions_target_currency"), 
        nullable = false,
        updatable = false
    )
    private Currency targetCurrency;

    @PrePersist
    void prePersist(){
        this.createdAt = OffsetDateTime.now(); 
    }

    public Transaction(String referenceNumber, String description, User user, Type type, Status status, Category category,
            BigDecimal exchangeRate, OffsetDateTime exchangeRateTime, Currency sourceCurrency, Currency targetCurrency) {

        referenceNumber = referenceNumber == null ? null : referenceNumber.trim();
        
        if(referenceNumber == null || referenceNumber.isBlank()) throw new IllegalArgumentException("Reference Number must not be blank");
        
        this.user = Objects.requireNonNull(user, "User must not be null");
        this.type = Objects.requireNonNull(type, "Type must not be null");
        this.status = Objects.requireNonNull(status, "Status must not be null");
        
        this.category = Objects.requireNonNull(category, "Category must not be null");
        if(!this.category.isAvailableFor(this.user)) throw new IllegalArgumentException("Category cannot be used by this user");

        this.sourceCurrency = Objects.requireNonNull(sourceCurrency,"Source currency must not be null");
        this.targetCurrency = Objects.requireNonNull(targetCurrency, "Target currency must not be null");
        this.exchangeRate = Objects.requireNonNull(exchangeRate, "Exchange rate must not be null");

        if (this.sourceCurrency.equals(this.targetCurrency)) {
            if (this.exchangeRate.compareTo(BigDecimal.ONE) != 0) {
                throw new IllegalArgumentException(
                        "Exchange rate must be 1 when currencies are the same");
            }
        } else {
            if (this.exchangeRate.signum() <= 0) {
                throw new IllegalArgumentException(
                        "Exchange rate must be greater than 0");
            }
            
        }
        
        this.exchangeRateTime = Objects.requireNonNull(exchangeRateTime,"Exchange rate time must not be null");
        this.referenceNumber = referenceNumber;
        this.description = description == null ? null : description.trim();
    }    
}
