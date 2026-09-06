package com.asluis.fintrack.fintrack.shared.infrastructure.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.data")
public record JWTProperties(String secretKey, int expirationMinutes)
{}
