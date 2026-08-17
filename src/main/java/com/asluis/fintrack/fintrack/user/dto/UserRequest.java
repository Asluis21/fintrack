package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.Role;

public record UserRequest(
        String emailEncrypted,
        String emailLookupHash,
        String passwordHash,
        Role role
) {
}
