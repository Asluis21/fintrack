package com.asluis.fintrack.fintrack.transaction.domain;

import java.util.Objects;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import com.asluis.fintrack.fintrack.common.domain.BaseEntity;
import com.asluis.fintrack.fintrack.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
    name    = "categories",
    schema  = "transaction_domain",
    uniqueConstraints = {
        @UniqueConstraint(
            name        =   "uk_category_name_createdBy", 
            columnNames =   {"name", "created_by", "accounting_type"}),
    }
)
@Entity
public class Category extends BaseEntity{

    @Column(
        length      = 100, 
        nullable    = false
    )
    private String name;
    
    @Column(
        name        = "accounting_type", 
        length      = 20, 
        nullable    = false,
        updatable   = false
    )
    @Enumerated(EnumType.STRING)
    private AccountingType accountingType;
    
    @Column(
        name        = "category_owner_type", 
        nullable    = false,
        updatable   = false
    )
    @Enumerated(EnumType.STRING)
    private CategoryOwnerType categoryOwnerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name        = "created_by", 
        updatable   = false, 
        foreignKey  = @ForeignKey(name="fk_category_created_by")
    )
    private User createdBy;

    public Category(String name, AccountingType accountingType, CategoryOwnerType ownerType, User createdBy) {
        this.name               = normalizeRequiredText(name, "Category name");
        this.accountingType     = Objects.requireNonNull(accountingType, "Accounting type must not be null");
        this.categoryOwnerType  = Objects.requireNonNull(ownerType, "Category Owner Type must not be null");
        this.createdBy          = createdBy;
        
        validateOwnership(ownerType, createdBy);
    }

    private static void validateOwnership(CategoryOwnerType ownerType, User createdBy) {

        if(ownerType == CategoryOwnerType.SYSTEM && createdBy != null){
            throw new IllegalArgumentException("System categories cannot have a creator");
        }

        if(ownerType == CategoryOwnerType.USER && createdBy == null){
            throw new IllegalArgumentException("User categories must have a creator");
        }
    }

    protected boolean isAvailableFor(User user){
        return categoryOwnerType == CategoryOwnerType.SYSTEM
                || createdBy.equals(user);
    }

    protected void rename(String name) {
        this.name = normalizeRequiredText(name, "Category name");
    }

}
