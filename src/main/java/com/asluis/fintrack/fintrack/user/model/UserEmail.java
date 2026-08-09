package com.asluis.fintrack.fintrack.user.model;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserEmail {
    
    @Column(
        name        = "email_encrypted", 
        nullable    = false, 
        length      = 500
    )
    private String emailEncrypted;
    
    @Getter(value = AccessLevel.PRIVATE)
    @Column(
        name        = "email_lookup_hash", 
        nullable    = false, 
        length      = 64
    )
    private String emailLookupHash;

    public UserEmail(String emailEncrypted, String emailLookupHash) {
        this.emailEncrypted     = normalizeRequiredText(emailEncrypted, "Email encrypted");
        this.emailLookupHash    = normalizeRequiredText(emailLookupHash, "Email lookup Hash");
    }
    
    protected final void changeEmail(String emailEncrypted, String emailLookupHash){
        this.emailEncrypted     = normalizeRequiredText(emailEncrypted, "Email encrypted");
        this.emailLookupHash    = normalizeRequiredText(emailLookupHash, "Email lookup Hash");
    }
}
