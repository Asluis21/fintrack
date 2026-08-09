package com.asluis.fintrack.fintrack.user.model;

import java.util.Objects;

import com.asluis.fintrack.fintrack.common.domain.BaseEntity;
import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name    = "users", 
    schema  = "user_domain",
    uniqueConstraints = {
        @UniqueConstraint(
            name        ="uk_user_email_lookup_hash", 
            columnNames = "email_lookup_hash")
    }
)
@Entity
public class User extends BaseEntity{
    
    @Embedded
    private UserEmail userEmail;

    @Column(
        nullable    = false, 
        length      = 255
    )
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable    = false, 
        length      = 20
    
    )
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    public User(UserEmail userEmail, String passwordHash, Role role) {
        this.userEmail      = Objects.requireNonNull(userEmail, "User Email is required");
        this.passwordHash   = normalizeRequiredText(passwordHash, "Password hash");
        this.role           = Objects.requireNonNull(role, "Role is required");
    }

    protected boolean isPasswordHashEquals(String paswordHash){
        return this.passwordHash.equals(paswordHash);
    }
    
    protected void changePassword(String passwordHash) {
        this.passwordHash = normalizeRequiredText(passwordHash, "Password hash");
    }
    
    protected void changeToUser() {
        this.role = Role.USER;
    }
    
    protected void changeToAdmin() {
        this.role = Role.ADMIN;
    }

    protected void activateUser(){
        this.status = Status.ACTIVE;   
    }

    protected void inactivateUser(){
        this.status = Status.INACTIVE;
    }

    protected void suspendUser(){
        this.status = Status.SUSPENDED;
    }
}
