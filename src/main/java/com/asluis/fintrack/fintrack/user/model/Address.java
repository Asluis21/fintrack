package com.asluis.fintrack.fintrack.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    name = "addresses", 
    schema = "user_domain", 
    uniqueConstraints = {
        @UniqueConstraint(
            name="uk_address_user", 
            columnNames = "user_id"
        )
    })
@Entity
public class Address {
    
    public Address(String street, String city, String postalCode, Country country, User user) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String street;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 20)
    private String postalCode;

    @ManyToOne
    @JoinColumn(
        name = "country_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name="fk_address_country")
    )
    private Country country;

    @OneToOne
    @JoinColumn(
        name = "user_id",
        nullable=false,
        foreignKey = @ForeignKey(name="fk_address_user")
    )
    private User user;

    protected void setStreet(String street) {
        this.street = street;
    }

    protected void setCity(String city) {
        this.city = city;
    }

    protected void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    protected void setCountry(Country country) {
        this.country = country;
    }
}
