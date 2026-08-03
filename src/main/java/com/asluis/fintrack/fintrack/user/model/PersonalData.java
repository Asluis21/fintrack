package com.asluis.fintrack.fintrack.user.model;

import java.time.LocalDate;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
    name = "personal_data", schema = "user_domain",
    uniqueConstraints={
        @UniqueConstraint(
            name="uk_document_country_number", 
            columnNames = {
                "document_country_id",
                "document_number_hash"
            })
    }
)
@Entity
public class PersonalData {

    public PersonalData(
        String name,
        String surname,
        String phone,
        Gender gender,
        LocalDate birthdate,
        Country documentCountry,
        String documentNumberHash,
        String documentNumberEncrypted,
        User user
    ) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.gender = gender;
        this.birthdate = birthdate;
        this.documentCountry = documentCountry;
        this.documentNumberHash = documentNumberHash;
        this.documentNumberEncrypted = documentNumberEncrypted;
        this.user = user;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String surname;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthdate;
    
    @ManyToOne
    @JoinColumn(
        name = "document_country_id", 
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_personal_data_document_country")
    )
    private Country documentCountry;

    @Column(name = "document_number_hash", length = 128, nullable = false)
    private String documentNumberHash;

    @Column(name = "document_number_encrypted", length = 500, nullable = false)
    private String documentNumberEncrypted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "personal_data_user_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name="fk_personal_data_user")
    )
    private User user;

    protected void changePhone(String phone) {
        this.phone = phone;
    }

    protected void changeName(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    
    public void updateDocumentDetails(Country country, String hash, String encrypted) {
        if (country == null || hash == null || encrypted == null) {
            throw new IllegalArgumentException("Document details cannot be null");
        }
        this.documentCountry = country;
        this.documentNumberHash = hash;
        this.documentNumberEncrypted = encrypted;
    }   
}
