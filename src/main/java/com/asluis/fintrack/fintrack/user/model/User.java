package com.asluis.fintrack.fintrack.user.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
    name = "users", 
    schema = "user_domain",
    uniqueConstraints = {
        @UniqueConstraint(name="uk_user_email_lookup_hash", columnNames = "email_lookup_hash")
    }
)
@Entity
public class User {

    public User(String emailEncrypted, String emailLookupHash, String passwordHash, Role role) {
        this.emailEncrypted = emailEncrypted;
        this.emailLookupHash = emailLookupHash;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_encrypted", nullable = false, length = 500)
    private String emailEncrypted;

    @Column(name = "email_lookup_hash", nullable = false, length = 64)
    private String emailLookupHash;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ACTIVE;


    protected void setEmailEncrypted(String emailEncrypted) {
        this.emailEncrypted = emailEncrypted;
    }

    protected void setEmailLookupHash(String emailLookupHash) {
        this.emailLookupHash = emailLookupHash;
    }

    protected void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    protected void setRole(Role role) {
        this.role = role;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    
}
