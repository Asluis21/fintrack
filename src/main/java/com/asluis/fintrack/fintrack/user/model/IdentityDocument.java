package com.asluis.fintrack.fintrack.user.model;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class IdentityDocument {
    
    @Column(
        name = "document_number_hash", 
        length = 128, 
        nullable = false
    )
    private String documentNumberHash;

    @Column(
        name = "document_number_encrypted", 
        length = 500, 
        nullable = false
    )
    private String documentNumberEncrypted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name        = "country_id", 
        nullable    = false,
        foreignKey  = @ForeignKey(name = "fk_personal_document_country")
    )
    private Country country;

    public IdentityDocument(String documentNumberHash, String documentNumberEncrypted, Country country) {
        this.documentNumberHash         = normalizeRequiredText(documentNumberHash, "Document number hash");
        this.documentNumberEncrypted    = normalizeRequiredText(documentNumberEncrypted, "Document number encrypted");
        this.country                    = Objects.requireNonNull(country, "Country is required");
    }
    
    protected String encryptedValue(){
        return this.documentNumberEncrypted;
    }

    public void updateDocumentDetails(Country country, String hash, String encrypted) {
        if (country == null || hash == null || encrypted == null) {
            throw new IllegalArgumentException("Document details cannot be null");
        }
        this.country                    = country;
        this.documentNumberHash         = hash;
        this.documentNumberEncrypted    = encrypted;
    }  
}
