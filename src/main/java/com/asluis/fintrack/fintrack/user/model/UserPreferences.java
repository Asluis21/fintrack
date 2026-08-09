package com.asluis.fintrack.fintrack.user.model;

import java.util.Objects;

import com.asluis.fintrack.fintrack.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name    = "user_preferences",
    schema  = "user_domain"
)
public class UserPreferences extends BaseEntity{
    
    @Column(
        name        = "dark_mode", 
        nullable    = false
    )
    private boolean darkMode = false; 
    
    @Enumerated(EnumType.STRING)
    @Column(
        name        = "language_code", 
        nullable    = false, 
        length      = 2
    )
    private LanguageCode languageCode = LanguageCode.EN;
    
    @Getter(value = AccessLevel.PRIVATE)
    @OneToOne
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name="fk_user_preference_user")
    )
    private User user;

    public UserPreferences(boolean darkMode, LanguageCode languageCode, User user) {
        this.darkMode       = darkMode;
        this.languageCode   = Objects.requireNonNull(languageCode, "Language code is required");
        this.user           = Objects.requireNonNull(user, "User is required");
    }
    
    protected void changeToDarkMode() {
        this.darkMode = true;
    }
    
    protected void changeToLightMode() {
        this.darkMode = false;
    }

    protected void changeLanguageCode(LanguageCode languageCode) {
        this.languageCode   = Objects.requireNonNull(languageCode, "Language code is required");
    }
}
