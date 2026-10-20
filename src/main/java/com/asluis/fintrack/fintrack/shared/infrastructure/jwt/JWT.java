package com.asluis.fintrack.fintrack.shared.infrastructure.jwt;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.common.exception.InvalidJWTException;
import com.asluis.fintrack.fintrack.shared.domain.JWTResponse;
import com.asluis.fintrack.fintrack.shared.domain.port.JWTService;
import com.asluis.fintrack.fintrack.user.domain.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWT implements JWTService{

    private final SecretKey secretKey;
    private final int expirationMinutes;
    

    public JWT(JWTProperties jwtProperties) {

        if (jwtProperties.secretKey() == null || jwtProperties.secretKey().isBlank()) {
            throw new IllegalStateException("jwt.data.secret-key is not configured");
        }

        if (jwtProperties.expirationMinutes() <= 0) {
            throw new IllegalStateException("jwt.data.expiration-minutes must be greater than 0");
        }

        try {
            // hmacShaKeyFor rejects keys shorter than 256 bits
            this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secretKey()));
        } catch (IllegalArgumentException | JwtException ex) {
            throw new IllegalStateException("Invalid JWT configuration", ex);
        }

        this.expirationMinutes = jwtProperties.expirationMinutes();
    }

    @Override
    public String generateToken(Long id, Role role) {

        return Jwts.builder()
            .subject(id.toString())
            .claim("role", role.name())
            .issuedAt(new Date())
            .expiration(new Date(new Date().getTime() + Duration.ofMinutes(expirationMinutes).toMillis()))
            .signWith(secretKey)
            .compact();
    }

    @Override
    public JWTResponse verifyToken(String token) throws InvalidJWTException {

        try{
            Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
            
            return new JWTResponse(claims.getSubject(), claims.get("role", String.class));

        }catch(JwtException  e){
            throw new InvalidJWTException("Invalid or expired token");
        }
    }
    
    
}
