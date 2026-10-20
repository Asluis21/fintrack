package com.asluis.fintrack.fintrack.shared.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asluis.fintrack.fintrack.shared.domain.port.JWTService;
import com.asluis.fintrack.fintrack.shared.dto.LoginResponse;
import com.asluis.fintrack.fintrack.shared.dto.LoginRequest;
import com.asluis.fintrack.fintrack.shared.dto.MeResponse;
import com.asluis.fintrack.fintrack.shared.dto.RegisterRequest;
import com.asluis.fintrack.fintrack.shared.security.domain.JwtPrincipal;
import com.asluis.fintrack.fintrack.shared.security.domain.SecurityUser;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.dto.UserMapper;
import com.asluis.fintrack.fintrack.user.dto.UserResponse;
import com.asluis.fintrack.fintrack.user.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController 
@RequestMapping("/auth")
@RequiredArgsConstructor 
public class AuthController {
    
    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){

        UsernamePasswordAuthenticationToken userVerfied = 
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        
        Authentication authUser = authenticationManager.authenticate(userVerfied);
    
        SecurityUser user = (SecurityUser) authUser.getPrincipal(); 

        addJwtCookie(response, user.getUser());
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletResponse response){

        User user = userService.register(registerRequest);

        addJwtCookie(response, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponse(user));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(@AuthenticationPrincipal JwtPrincipal principal){
        return ResponseEntity.ok(new MeResponse(userService.findUsername(principal.userId())));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){

        removeCookie(response);

        return ResponseEntity.noContent().build();
    }

    private void addJwtCookie(HttpServletResponse response, User user){

        String token = jwtService.generateToken(user.getId(), user.getRole());

        ResponseCookie cookie = ResponseCookie.from("jwt-token", token)
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .path("/")
            .maxAge(Duration.ofHours(1))
            .build();        

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

        private void removeCookie(HttpServletResponse response){
            ResponseCookie cookie = ResponseCookie.from("jwt-token", "")
                .maxAge(0)
                .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
}
