package com.asluis.fintrack.fintrack.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity
@Table(
    name = "countries", 
    schema = "user_domain",
    uniqueConstraints = {
        @UniqueConstraint(name="uk_country_name", columnNames = "name"),
        @UniqueConstraint(name="uk_country_iso_code", columnNames = "iso_code"),
    }
)
public class Country {

    public Country(String name, String isoCode) {
        this.name = name;
        this.isoCode = isoCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    // ISO 3166-1 alpha-3 standard EG. CAN, USA, FRA
    @Column(length = 3, nullable = false)
    private String isoCode;

    
    protected void setName(String name) {
        this.name = name;
    }

    protected void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }
}
