package com.asluis.fintrack.fintrack.shared.infrastructure.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "encryption.data")
public record AesGcmProperties(
    int tagLengthBits,
    int ivLengthBytes,
    String secretKey
){}

