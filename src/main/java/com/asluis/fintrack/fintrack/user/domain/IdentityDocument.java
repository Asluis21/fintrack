package com.asluis.fintrack.fintrack.user.domain;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import java.util.Objects;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class IdentityDocument {
    
    @Getter(value = AccessLevel.NONE)
    @Column(
        name        = "document_number_hash", 
        length      = 128, 
        nullable    = false
    )
    private String documentNumberHash;

    @Getter(value = AccessLevel.NONE)
    @Column(
        name        = "document_number_encrypted", 
        length      = 500, 
        nullable    = false
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

    protected String decryptDocumentNumber(EncryptionService encryptionService){
        return encryptionService.decrypt(this.documentNumberEncrypted);
    }

    protected void updateDocumentDetails(Country country, String hash, String encrypted) {
        if (country == null || hash == null || encrypted == null) {
            throw new IllegalArgumentException("Document details cannot be null");
        }
        this.country                    = Objects.requireNonNull(country, "Country is required");   
        this.documentNumberHash         = normalizeRequiredText(hash, "Document number hash");
        this.documentNumberEncrypted    = normalizeRequiredText(encrypted, "Document number encrypted");
    }  
}
