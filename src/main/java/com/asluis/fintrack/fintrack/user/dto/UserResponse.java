package com.asluis.fintrack.fintrack.user.dto;

import java.time.OffsetDateTime;

import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.Status;

public record UserResponse(
        Long id,
        Role role,
        Status status,
        OffsetDateTime createdAt
) {
}
