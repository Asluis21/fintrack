package com.asluis.fintrack.fintrack.shared.domain;

import java.util.Locale;
import java.util.regex.Pattern;

import com.asluis.fintrack.fintrack.common.domain.BaseEntity;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.validateRequired;

import jakarta.persistence.CheckConstraint;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Currency extends BaseEntity{

    private static final Pattern ISO_CODE_PATTERN = Pattern.compile("[A-Z]{3}");

    @Column(length = 100, nullable = false, updatable = false)
    private String name;

    // ISO 4217 (currency codes like PEN, USD, or EUR)
    @Column(name ="iso_code", length = 3, nullable = false, updatable = false)
    private String isoCode;
    
    @Column(name = "decimal_places", nullable = false, updatable = false)
    private int decimalPlaces;


    public Currency(String name, String isoCode, int decimalPlaces) {

        this.name = validateRequired(name, "Currency name").replaceAll("\\s+", " ");;

        this.isoCode = validateRequired(isoCode, "ISO code").toUpperCase(Locale.ROOT);

        if(!ISO_CODE_PATTERN.matcher(isoCode).matches()){
            throw new IllegalArgumentException("ISO code must contain exactly 3 uppercase letters");
        }

        if(decimalPlaces < 0){
            throw new IllegalArgumentException("Decimal places must not be negative");
        }

        this.decimalPlaces = decimalPlaces;
    }
}
