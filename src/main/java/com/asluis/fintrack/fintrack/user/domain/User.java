package com.asluis.fintrack.fintrack.user.domain;

import java.util.Objects;

import com.asluis.fintrack.fintrack.common.domain.BaseEntity;
import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Getter(value = AccessLevel.NONE)
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

    @Builder
    public User(String passwordHash, Role role, UserEmail email) {
        this.passwordHash   = normalizeRequiredText(passwordHash, "Password hash");
        this.userEmail      = Objects.requireNonNull(email, "Email is required");
        this.role           = Objects.requireNonNull(role, "Role is required");
    }
    
    public String decryptEmail(EncryptionService encryptionService){
        return userEmail.decryptEmail(encryptionService);
    }

    public void changeEmail(UserEmail userEmail) {
        this.userEmail = Objects.requireNonNull(userEmail, "Email is required");
    }

    public void changePassword(String passwordHash) {
        this.passwordHash = normalizeRequiredText(passwordHash, "Password hash");
    }

    public void changeToUser() {
        this.role = Role.USER;
    }

    public void changeToAdmin() {
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
