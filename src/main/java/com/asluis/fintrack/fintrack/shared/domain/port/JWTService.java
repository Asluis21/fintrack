package com.asluis.fintrack.fintrack.shared.domain.port;

import com.asluis.fintrack.fintrack.common.exception.InvalidJWTException;
import com.asluis.fintrack.fintrack.shared.domain.JWTResponse;
import com.asluis.fintrack.fintrack.user.domain.Role;

public interface JWTService {
    
    String generateToken(Long id, Role role);
    
    JWTResponse verifyToken(String token) throws InvalidJWTException;
}
