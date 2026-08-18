package com.asluis.fintrack.fintrack.user.domain;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserEmail {

    @Column(
        name        = "email_encrypted",
        nullable    = false,
        length      = 500
    )
    private String emailEncrypted;

    @Column(
        name        = "email_lookup_hash",
        nullable    = false,
        length      = 64
    )
    private String emailLookupHash;

    public UserEmail(String email, EncryptionService encryptionService, HashService hashService) {
        String normalizedEmail = normalizeRequiredText(email, "Email");
        this.emailEncrypted     = encryptionService.encrypt(normalizedEmail);
        this.emailLookupHash    = hashService.hash(normalizedEmail);
    }

    protected final void changeEmail(String email, EncryptionService encryptionService, HashService hashService){
        String normalizedEmail = normalizeRequiredText(email, "Email");
        this.emailEncrypted     = encryptionService.encrypt(normalizedEmail);
        this.emailLookupHash    = hashService.hash(normalizedEmail);
    }

    protected String decryptEmail(EncryptionService encryptionService){
        return encryptionService.decrypt(emailEncrypted);
    }
}
