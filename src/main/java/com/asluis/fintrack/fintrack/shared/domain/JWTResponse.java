package com.asluis.fintrack.fintrack.shared.domain;

public record JWTResponse(
    String subject,
    String role
) {
}
