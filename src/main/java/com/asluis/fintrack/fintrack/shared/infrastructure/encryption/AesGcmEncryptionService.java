package com.asluis.fintrack.fintrack.shared.infrastructure.encryption;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.common.exception.EncryptionException;

@Service
public class AesGcmEncryptionService implements EncryptionService{

    private final AesGcmProperties aesGcmProperties;
    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();
    private static final String KEY_ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    

    public AesGcmEncryptionService(AesGcmProperties aesGcmProperties) {

        if(aesGcmProperties.ivLengthBytes() != 12){
            throw new IllegalArgumentException("AES-GCM IV must be 12 bytes");
        }

        if(aesGcmProperties.tagLengthBits() != 128){
            throw new IllegalArgumentException("AES-GCM tag must be 128 bits");
        }
        
        this.aesGcmProperties = aesGcmProperties;

        try{
            byte[] key = Base64.getDecoder().decode(aesGcmProperties.secretKey());
            
    
            if(key.length != 32){
                throw new IllegalArgumentException("A 32-byte key is required");
            }

            this.secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        }catch(IllegalArgumentException ex){
            throw new IllegalStateException("Invalid AES configuration", ex);
        }

    }

    @Override
    public String decrypt(String value) {

        Objects.requireNonNull(value, "Value must not be null");

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value must not be blank");
        }

        try{
            byte[] decoded = Base64.getDecoder().decode(value);
    
            if (decoded.length < aesGcmProperties.ivLengthBytes()) {
                throw new EncryptionException("Invalid encrypted value");
            }
    
            ByteBuffer buffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[aesGcmProperties.ivLengthBytes()];
            buffer.get(iv);
    
            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(
                Cipher.DECRYPT_MODE, 
                secretKey,
                new GCMParameterSpec(aesGcmProperties.tagLengthBits(), iv)
            );
            
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        }catch(IllegalArgumentException | GeneralSecurityException ex){
            throw new EncryptionException("Unable to decrypt data", ex);
        }

    }

    @Override
    public String encrypt(String value) {

        Objects.requireNonNull(value, "Value must not be null");

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value must not be blank");
        }
        
        byte[] iv = new byte[aesGcmProperties.ivLengthBytes()];
        try{
            secureRandom.nextBytes(iv);
    
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(
                Cipher.ENCRYPT_MODE, 
                secretKey,
                new GCMParameterSpec(aesGcmProperties.tagLengthBits(), iv)
            );

            byte[] cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
    
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + cipherText.length);
            return Base64.getEncoder().encodeToString(buffer.put(iv).put(cipherText).array());
        }catch(IllegalArgumentException | GeneralSecurityException ex){
            throw new EncryptionException("Unable to encrypt data", ex);

        }
    }
}