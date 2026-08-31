package com.asluis.fintrack.fintrack.user.domain;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import java.util.Objects;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

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

    public IdentityDocument(String documentNumber, Country country, EncryptionService encryptionService, HashService hashService) {
        String normalizedDocumentNumber = normalizeRequiredText(documentNumber, "Document number");
        this.documentNumberEncrypted    = encryptionService.encrypt(normalizedDocumentNumber);
        this.documentNumberHash         = hashService.hash(normalizedDocumentNumber);
        this.country                    = Objects.requireNonNull(country, "Country is required");
    }

    protected String decryptDocumentNumber(EncryptionService encryptionService){
        return encryptionService.decrypt(this.documentNumberEncrypted);
    }

    protected void updateDocumentDetails(Country country, String documentNumber, EncryptionService encryptionService, HashService hashService) {
        String normalizedDocumentNumber = normalizeRequiredText(documentNumber, "Document number");
        this.country                    = Objects.requireNonNull(country, "Country is required");
        this.documentNumberEncrypted    = encryptionService.encrypt(normalizedDocumentNumber);
        this.documentNumberHash         = hashService.hash(normalizedDocumentNumber);
    }
}
