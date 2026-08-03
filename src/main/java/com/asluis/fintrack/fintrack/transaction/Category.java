package com.asluis.fintrack.fintrack.transaction;

import java.util.Objects;

import com.asluis.fintrack.fintrack.user.model.User;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
    name = "categories",
    schema = "transaction_domain",
    uniqueConstraints = {
        @UniqueConstraint(name="uk_category_name_createdBy", columnNames = {"name", "created_by", "accounting_type"})
    }
)
@Entity
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;
    
    @Column(name = "accounting_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountingType accountingType;
    
    @Column(name = "category_owner_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryOwnerType categoryOwnerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "created_by", 
        nullable = true,
        foreignKey = @ForeignKey(name="fk_category_created_by")
    )
    private User createdBy;

    public Category(String name, AccountingType accountingType, CategoryOwnerType categoryOwnerType, User createdBy) {
        this.name = Objects.requireNonNull(name, "Category name must not be null");
        this.accountingType = Objects.requireNonNull(accountingType, "Accounting type must not be null");
        this.categoryOwnerType = Objects.requireNonNull(categoryOwnerType, "Category Owner Type must not be null");
        this.createdBy = createdBy;
    }

    protected boolean isAvailableFor(User user){
        return categoryOwnerType == CategoryOwnerType.SYSTEM
                || Objects.equals(createdBy, user);
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setAccountingType(AccountingType accountingType) {
        this.accountingType = accountingType;
    }

}
