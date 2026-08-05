package com.asluis.fintrack.fintrack.user.model;

import java.time.LocalDate;
import java.util.Objects;

import com.asluis.fintrack.fintrack.common.domain.BaseEntity;
import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
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
public class PersonalData extends BaseEntity{

    @Column(
        length      = 100, 
        nullable    = false
    )
    private String name;

    @Column(
        length      = 100, 
        nullable    = false
    )
    private String surname;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(
        length      = 10, 
        nullable    = false
    )
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthdate;
    
    @Embedded
    private IdentityDocument identityDocument;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "personal_data_user_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name="fk_personal_data_user")
    )
    private User user;

    public PersonalData(
        String name,
        String surname,
        String phone,
        Gender gender,
        IdentityDocument identityDocument,
        LocalDate birthdate,
        User user
    ) {
        this.name               = normalizeRequiredText(name, "Name");
        this.surname            = normalizeRequiredText(surname, "Surname");
        this.phone              = normalizeRequiredText(phone, "Phone");
        this.gender             = Objects.requireNonNull(gender, "Gender is required");
        this.identityDocument   = Objects.requireNonNull(identityDocument, "Identity document is required");
        this.birthdate          = Objects.requireNonNull(birthdate, "Birthdate is required");
        this.user               = Objects.requireNonNull(user, "User is required");
    }

    protected void changePhone(String phone) {
        this.phone = normalizeRequiredText(phone, "Phone");
    }

    protected void changeName(String name, String surname) {
        this.name       = normalizeRequiredText(name, "Name");
        this.surname    = normalizeRequiredText(surname, "Surname");
    }

    public void changeGender(Gender gender) {
        this.gender = Objects.requireNonNull(gender, "Gender is required");
    }

    public void changeBirthdate(LocalDate birthdate) {
        this.birthdate  = Objects.requireNonNull(birthdate, "Birthdate is required");
    } 
}
