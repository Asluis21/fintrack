package com.asluis.fintrack.fintrack.user.domain;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.*;

import java.util.Objects;

import com.asluis.fintrack.fintrack.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    name    = "addresses", 
    schema  = "user_domain", 
    uniqueConstraints = {
        @UniqueConstraint(
            name        = "uk_address_user", 
            columnNames = "user_id"
        )
    })
@Entity
public class Address extends BaseEntity{

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String street;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 20)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name        = "country_id", 
        nullable    = false, 
        updatable   = false,
        foreignKey  = @ForeignKey(name="fk_address_country")
    )
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name        = "user_id",
        nullable    = false,
        foreignKey  = @ForeignKey(name="fk_address_user"),
        updatable   = false
    )
    private User user;

    public Address(String name, String street, String city, String postalCode, Country country, User user) {
        
        this.name   = validateRequired(name, "name");
        this.street = validateRequired(street, "Street");
        this.city   = validateRequired(city, "City");

        this.postalCode = normalizeOptional(postalCode);

        this.country    = Objects.requireNonNull(country, "Country must not be null");
        this.user       = Objects.requireNonNull(user, "User must not be null");
    }

    public void changeAddressName(String name) {
        this.name = validateRequired(name, "Address Name");
    }

    public void changeStreet(String street) {
        this.street = validateRequired(street, "Street");
    }

    public void changeCity(String city) {
        this.city =  validateRequired(city, "City");
    }

    public void changePostalCode(String postalCode) {
        this.postalCode = normalizeOptional(postalCode);
    }
}
