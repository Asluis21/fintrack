package com.asluis.fintrack.fintrack.shared.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.asluis.fintrack.fintrack.account.controller.AccountController;
import com.asluis.fintrack.fintrack.account.domain.AccountType;
import com.asluis.fintrack.fintrack.account.domain.Status;
import com.asluis.fintrack.fintrack.account.dto.AccountResponse;
import com.asluis.fintrack.fintrack.account.service.AccountService;
import com.asluis.fintrack.fintrack.common.exception.InvalidJWTException;
import com.asluis.fintrack.fintrack.shared.domain.JWTResponse;
import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.domain.port.JWTService;
import com.asluis.fintrack.fintrack.shared.domain.port.PasswordHasher;
import com.asluis.fintrack.fintrack.shared.dto.LoginRequest;
import com.asluis.fintrack.fintrack.shared.security.SecurityConfig;
import com.asluis.fintrack.fintrack.shared.security.domain.SecurityUser;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest (AuthController.class)
@Import (SecurityConfig.class)
class AuthControllerTest {
    
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private AuthenticationManager authenticationManager;
    
    @MockitoBean  
    private  JWTService jwtService;
    
    @MockitoBean 
    private PasswordHasher passwordHasher;

    @MockitoBean 
    private EncryptionService encryptionService;

    @MockitoBean 
    private HashService hashService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test 
    void loginSuccedsAndReturnsToken() throws Exception {
        
        User user = new User(
            "password", 
            Role.USER, 
            new UserEmail("test@correo.com", encryptionService, hashService)
        );

        SecurityUser securityUser = new SecurityUser(user, "test@correo.com");
        Authentication authResult = new UsernamePasswordAuthenticationToken(securityUser, null);

        when(encryptionService.encrypt(anyString())).thenReturn("encrypt test");
        when(encryptionService.encrypt(anyString())).thenReturn("hash test");
        when(authenticationManager.authenticate(any())).thenReturn(authResult);
        when(jwtService.generateToken(user.getId(), user.getRole())).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new LoginRequest("test@correo.com", "password")))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    
    }

    @Test
    void loginFailsWithBadCredentials() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad credentials"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("test@test.com", "wrong"))))
            .andExpect(status().isUnauthorized());
    }


    @Test
    void meWithoutTokenisRejected() throws Exception {
        mockMvc.perform(get("/account/me"))
            .andExpect(status().isForbidden());
    }

}
