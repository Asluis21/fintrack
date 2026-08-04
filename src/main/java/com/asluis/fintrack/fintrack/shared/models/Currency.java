package com.asluis.fintrack.fintrack.shared.models;

import java.util.Locale;
import java.util.Objects;

import jakarta.persistence.CheckConstraint;

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
@Table(
        name="currencies",
        schema = "shared",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_currency_name", columnNames = "name"),
            @UniqueConstraint(name = "uk_currency_iso_code", columnNames = "iso_code")
        },
        check = {
            @CheckConstraint(name="chk_currency_decimal_places", constraint = "decimal_places >= 0")
        }
)
@Entity
public class Currency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, updatable = false)
    private String name;

    // ISO 4217 (currency codes like PEN, USD, or EUR)
    @Column(name ="iso_code", length = 3, nullable = false, updatable = false)
    private String isoCode;
    
    @Column(name = "decimal_places", nullable = false, updatable = false)
    private int decimalPlaces;


    public Currency(String name, String isoCode, int decimalPlaces) {
        name = Objects.requireNonNull(name, "Currency name must not be null").trim();

        if(name.isBlank()){
            throw new IllegalArgumentException("Currency name must not be blank");
        }

        isoCode = Objects.requireNonNull(isoCode, "ISO code must not be null")
                        .trim()
                        .toUpperCase(Locale.ROOT);

        if(!isoCode.matches("[A-Z]{3}")){
            throw new IllegalArgumentException("ISO code must contain exactly 3 uppercase letters");
        }

        if(decimalPlaces < 0){
            throw new IllegalArgumentException("Decimal places must not be negative");
        }

        this.name = name.trim().replaceAll("\\s+", " ");
        this.isoCode = isoCode;
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Currency other)){
            return false;
        }
        
        return id != null && id.equals(other.id);
    }    
}
