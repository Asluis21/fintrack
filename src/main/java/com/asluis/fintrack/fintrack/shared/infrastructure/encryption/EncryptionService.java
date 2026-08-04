package com.asluis.fintrack.fintrack.shared.infrastructure.encryption;

public interface EncryptionService {
    
    String encrypt(String value);
    String decrypt(String value);
}
