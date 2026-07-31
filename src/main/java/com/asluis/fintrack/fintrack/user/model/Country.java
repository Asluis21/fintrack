package com.asluis.fintrack.fintrack.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    // ISO 3188 -1 alpha-3
    @Column(length = 3, nullable = false)
    private String isoCode;
}
