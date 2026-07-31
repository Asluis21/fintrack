package com.asluis.fintrack.fintrack.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(
    name = "user_preferences",
    schema = "user_domain"
)
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dark_mode", nullable = false)
    private boolean darkMode = false; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "language_code", nullable = false, length = 2)
    private LanguageCode languageCode = LanguageCode.EN;
}
