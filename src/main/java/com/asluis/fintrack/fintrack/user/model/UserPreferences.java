package com.asluis.fintrack.fintrack.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    name = "user_preferences",
    schema = "user_domain"
)
public class UserPreferences {

    public UserPreferences(boolean darkMode, LanguageCode languageCode, User user) {
        this.darkMode = darkMode;
        this.languageCode = languageCode;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dark_mode", nullable = false)
    private boolean darkMode = false; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "language_code", nullable = false, length = 2)
    private LanguageCode languageCode = LanguageCode.EN;

    @OneToOne
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name="fk_user_preference_user")
    )
    private User user;

    protected void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    protected void setLanguageCode(LanguageCode languageCode) {
        this.languageCode = languageCode;
    }
}
