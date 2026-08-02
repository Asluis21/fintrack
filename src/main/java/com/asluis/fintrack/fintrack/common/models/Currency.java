package com.asluis.fintrack.fintrack.common.models;

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
    
    public Currency(String name, String isoCode, int decimalPlaces) {
        this.name = name;
        this.isoCode = isoCode;

        if(decimalPlaces < 0){
            throw new IllegalArgumentException("Decimal places must not be negative");
        }

        this.decimalPlaces = decimalPlaces;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    // ISO 4217 (currency codes like PEN, USD, or EUR)
    @Column(name ="iso_code", length = 3, nullable = false)
    private String isoCode;
    
    @Column(name = "decimal_places", nullable = false)
    private int decimalPlaces;

    
    protected void setName(String name) {
        this.name = name;
    }

    protected void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    protected void setDecimalPlaces(int decimalPlaces) {
        
        if(decimalPlaces < 0){
            throw new IllegalArgumentException("Decimal places must not be negative");
        }
        
        this.decimalPlaces = decimalPlaces;
    }

}
