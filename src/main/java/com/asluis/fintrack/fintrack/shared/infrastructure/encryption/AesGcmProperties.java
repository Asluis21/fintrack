package com.asluis.fintrack.fintrack.shared.infrastructure.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "encryption.data")
public record AesGcmProperties(
    String algorithm,
    int tagLengthBits,
    int ivLengthBytes,
    String secretKey
){}

