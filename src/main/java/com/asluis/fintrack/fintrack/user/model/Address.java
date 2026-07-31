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
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(
    name = "addresses", 
    schema = "user_domain", 
    uniqueConstraints = {
        @UniqueConstraint(
            name="uk_address_personal_data", 
            columnNames = "personal_data_id"
        )
    })
@Entity
public class Address {
    
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
        name = "personal_data_id",
        nullable=false,
        foreignKey = @ForeignKey(name="fk_address_personal_data")
    )
    private PersonalData personalData;
}
