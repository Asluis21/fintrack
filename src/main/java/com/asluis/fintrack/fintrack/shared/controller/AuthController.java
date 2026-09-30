package com.asluis.fintrack.fintrack.shared.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asluis.fintrack.fintrack.shared.domain.port.JWTService;
import com.asluis.fintrack.fintrack.shared.dto.LoginResponse;
import com.asluis.fintrack.fintrack.shared.dto.LoginRequest;
import com.asluis.fintrack.fintrack.shared.security.domain.SecurityUser;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController 
@RequestMapping("/auth")
@RequiredArgsConstructor 
public class AuthController {
    
    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){

        UsernamePasswordAuthenticationToken userVerfied = 
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        
        Authentication authUser = authenticationManager.authenticate(userVerfied);
    
        SecurityUser user = (SecurityUser) authUser.getPrincipal(); 

        String token = jwtService.generateToken(user.getUser().getId(), user.getUser().getRole());

        ResponseCookie cookie = ResponseCookie.from("jwt-token", token)
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .path("/")
            .maxAge(Duration.ofHours(1))
            .build();        

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        
        return ResponseEntity.ok().build();
    }

}
