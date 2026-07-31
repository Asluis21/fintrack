package com.asluis.fintrack.fintrack.user.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(
    name = "personal_data", schema = "user_domain",
    uniqueConstraints={
        @UniqueConstraint(
            name="uk_document_country_number", 
            columnNames = {
                "document_country_id",
                "document_number"
            })
    }
)
@Entity
public class PersonalData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String surname;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthdate;
    
    @ManyToOne
    @JoinColumn(
        name = "document_country_id", 
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_personal_data_document_country")
    )
    private Country documentCountry;

    @Column(length = 20, nullable = false)
    private String document_number;

}
