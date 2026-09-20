package com.asluis.fintrack.fintrack.shared.security.infrastructure;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.asluis.fintrack.fintrack.shared.domain.port.PasswordHasher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpringSecurityPasswordEncoder implements PasswordEncoder {

    private final PasswordHasher passwordHasher;

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordHasher.hash(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordHasher.matches(
            rawPassword.toString(),
            encodedPassword
        );
    }
}