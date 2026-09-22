package com.asluis.fintrack.fintrack.shared.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.asluis.fintrack.fintrack.account.controller.AccountController;
import com.asluis.fintrack.fintrack.account.domain.AccountType;
import com.asluis.fintrack.fintrack.account.domain.Status;
import com.asluis.fintrack.fintrack.account.dto.AccountResponse;
import com.asluis.fintrack.fintrack.account.service.AccountService;
import com.asluis.fintrack.fintrack.common.exception.InvalidJWTException;
import com.asluis.fintrack.fintrack.shared.domain.JWTResponse;
import com.asluis.fintrack.fintrack.shared.domain.port.JWTService;
import com.asluis.fintrack.fintrack.shared.domain.port.PasswordHasher;
import com.asluis.fintrack.fintrack.shared.security.SecurityConfig;

@WebMvcTest(AccountController.class)
@Import(SecurityConfig.class)
public class AccountControllerTest {

    @MockitoBean 
    private AccountService accountService;
    
    @MockitoBean  
    private  JWTService jwtService;

    @MockitoBean 
    private PasswordHasher passwordHasher;

    @Autowired 
    private MockMvc mockMvc;

    
    @Test 
    void meWithValidTokenReturnsAccount() throws Exception {

        AccountResponse response = new AccountResponse(
            1L, 1L, 1L, 
            AccountType.CHECKING, 
            Status.ACTIVE, BigDecimal.TEN,
            OffsetDateTime.now()
        );

        when(jwtService.verifyToken("valid-token")).thenReturn(new JWTResponse("1", "USER"));
        when(accountService.findById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/account/me")
            .header("Authorization", "Bearer valid-token"))
            .andExpect(status().isOk());
    }

    @Test 
    void meWithInvalidTokenisRejected() throws Exception {
        when(jwtService.verifyToken("bad-token")).thenThrow(new InvalidJWTException("Invalid or expired token"));

        mockMvc.perform(get("/account/me").header("Authorization", "Bearer bad-token"))
            .andExpect(status().isForbidden());
    }
    
}
