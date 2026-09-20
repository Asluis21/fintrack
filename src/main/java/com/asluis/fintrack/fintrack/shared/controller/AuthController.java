package com.asluis.fintrack.fintrack.shared.controller;

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

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController 
@RequestMapping("/auth")
@RequiredArgsConstructor 
public class AuthController {
    
    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        UsernamePasswordAuthenticationToken userVerfied = 
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        
        Authentication authUser = authenticationManager.authenticate(userVerfied);
    
        SecurityUser user = (SecurityUser) authUser.getPrincipal(); 

        String token = jwtService.generateToken(user.getUser().getId(), user.getUser().getRole());

        return ResponseEntity.ok(new LoginResponse(token));
    }

}
