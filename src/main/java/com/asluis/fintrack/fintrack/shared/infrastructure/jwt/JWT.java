package com.asluis.fintrack.fintrack.shared.infrastructure.jwt;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

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

    private String secretKey;
    private int expirationMinutes;
    

    public JWT(JWTProperties jwtProperties) {

        this.secretKey = jwtProperties.secretKey();
        this.expirationMinutes = jwtProperties.expirationMinutes();
    }

    @Override
    public String generateToken(Long id, Role role) {

        return Jwts.builder()
            .subject(id.toString())
            .claim("role", role.name())
            .issuedAt(new Date())
            .expiration(new Date(new Date().getTime() + Duration.ofMinutes(expirationMinutes).toMillis()))
            .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)))
            .compact();
    }

    @Override
    public JWTResponse verifyToken(String token) throws InvalidJWTException {

        try{
            Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)))
            .build()
            .parseSignedClaims(token)
            .getPayload();
            
            return new JWTResponse(claims.getSubject(), claims.get("role", String.class));

        }catch(JwtException  e){
            throw new InvalidJWTException("Invalid or expired token");
        }
    }
    
    
}
