package com.asluis.fintrack.fintrack.shared.security.domain;

import com.asluis.fintrack.fintrack.user.domain.Role;

public record JwtPrincipal(Long userId, Role role) {}

