package com.asluis.fintrack.fintrack.shared.infrastructure.hashing;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hashing.data")
public record HmacProperties(
    String secretKey
){}
