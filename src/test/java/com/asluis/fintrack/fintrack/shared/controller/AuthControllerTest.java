package com.asluis.fintrack.fintrack.shared.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.domain.port.JWTService;
import com.asluis.fintrack.fintrack.shared.domain.port.PasswordHasher;
import com.asluis.fintrack.fintrack.shared.dto.LoginRequest;
import com.asluis.fintrack.fintrack.shared.dto.RegisterRequest;
import com.asluis.fintrack.fintrack.shared.exception.EmailAlreadyRegisteredException;
import com.asluis.fintrack.fintrack.shared.security.SecurityConfig;
import com.asluis.fintrack.fintrack.shared.security.domain.SecurityUser;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.service.UserService;
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

    @MockitoBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test 
    void loginSucceedsAndSetsJwtCookie() throws Exception {

        when(encryptionService.encrypt(anyString())).thenReturn("encrypt test");
        when(hashService.hash(anyString())).thenReturn("hash test");

        User user = new User(
            "password", 
            Role.USER, 
            new UserEmail("test@correo.com", encryptionService, hashService)
        );

        SecurityUser securityUser = new SecurityUser(user, "test@correo.com");
        Authentication authResult = new UsernamePasswordAuthenticationToken(securityUser, null);

        when(authenticationManager.authenticate(any())).thenReturn(authResult);
        when(jwtService.generateToken(user.getId(), user.getRole())).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new LoginRequest("test@correo.com", "password")))
        ).andExpect(status().isOk())
        .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("jwt-token=fake-jwt-token")))
        .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("HttpOnly")));
    
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
    void registerCreatesUserAndSetsJwtCookie() throws Exception {

        when(encryptionService.encrypt(anyString())).thenReturn("encrypt test");
        when(hashService.hash(anyString())).thenReturn("hash test");

        User user = new User(
            "password", 
            Role.USER, 
            new UserEmail("new@correo.com", encryptionService, hashService)
        );

        when(userService.register(any(RegisterRequest.class))).thenReturn(user);
        when(jwtService.generateToken(eq(user.getId()), eq(Role.USER))).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new RegisterRequest("new@correo.com", "password123")))
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.role").value("USER"))
        .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("jwt-token=fake-jwt-token")));
    }

    @Test
    void registerFailsWhenEmailAlreadyExists() throws Exception {
        when(userService.register(any(RegisterRequest.class))).thenThrow(new EmailAlreadyRegisteredException());

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest("taken@correo.com", "password123"))))
            .andExpect(status().isConflict());
    }

    @Test
    void registerFailsWithInvalidBody() throws Exception {
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest("not-an-email", "short"))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void meWithoutTokenisRejected() throws Exception {
        mockMvc.perform(get("/account/me"))
            .andExpect(status().isForbidden());
    }

}
