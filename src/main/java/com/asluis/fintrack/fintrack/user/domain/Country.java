package com.asluis.fintrack.fintrack.user.domain;

import com.asluis.fintrack.fintrack.common.domain.BaseEntity;
import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Country extends BaseEntity{

    @Column(
        length      = 100, 
        nullable    = false
    )
    private String name;

    // ISO 3166-1 alpha-3 standard EG. CAN, USA, FRA
    @Column(
        length      = 3, 
        nullable    = false)
    private String isoCode;

    public Country(String name, String isoCode) {
        this.name       = normalizeRequiredText(name, "Country name");
        this.isoCode    = normalizeRequiredText(isoCode, "ISO code");
    }

    public void rename(String name) {
        this.name = normalizeRequiredText(name, "Country name");
    }

    public void updateIsoCode(String isoCode) {
        this.isoCode = normalizeRequiredText(isoCode, "ISO code");
    }
}
