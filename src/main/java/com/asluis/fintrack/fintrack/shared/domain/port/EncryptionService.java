package com.asluis.fintrack.fintrack.shared.domain.port;

public interface EncryptionService {
    
    String encrypt(String value);
    String decrypt(String value);
}
