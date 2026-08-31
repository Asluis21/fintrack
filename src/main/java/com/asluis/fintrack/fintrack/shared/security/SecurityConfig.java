package com.asluis.fintrack.fintrack.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.asluis.fintrack.fintrack.shared.domain.port.PasswordHasher;
import com.asluis.fintrack.fintrack.shared.security.infrastructure.SpringSecurityPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(PasswordHasher passwordHasher) {
        return new SpringSecurityPasswordEncoder(passwordHasher);
    }
}
