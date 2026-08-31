package com.asluis.fintrack.fintrack.shared.infrastructure.hashing;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.common.exception.EncryptionException;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;

@Service
public class HmacSha256HashService implements HashService {

    private static final String ALGORITHM = "HmacSHA256";

    private final SecretKeySpec secretKey;

    public HmacSha256HashService(HmacProperties hmacProperties) {

        byte[] key;
        try {
            key = Base64.getDecoder().decode(hmacProperties.secretKey());
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Invalid HMAC configuration", ex);
        }

        if (key.length < 32) {
            throw new IllegalStateException("HMAC secret key must be at least 32 bytes");
        }

        this.secretKey = new SecretKeySpec(key, ALGORITHM);
    }

    @Override
    public String hash(String value) {

        Objects.requireNonNull(value, "Value must not be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("Value must not be blank");
        }

        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKey);

            byte[] digest = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(digest);
        } catch (GeneralSecurityException ex) {
            throw new EncryptionException("Unable to hash data", ex);
        }
    }
}
