package com.asluis.fintrack.fintrack.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.asluis.fintrack.fintrack.shared.domain.port.JWTService;
import com.asluis.fintrack.fintrack.shared.domain.port.PasswordHasher;
import com.asluis.fintrack.fintrack.shared.security.infrastructure.JwtAuthenticationFilter;
import com.asluis.fintrack.fintrack.shared.security.infrastructure.SpringSecurityPasswordEncoder;

@Configuration
@EnableWebSecurity 
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(PasswordHasher passwordHasher) {
        return new SpringSecurityPasswordEncoder(passwordHasher);
    }

    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http, JWTService jwtService) throws Exception{
        return http
            .csrf(csrf -> csrf.disable()) // To disable a malicious site piggybacking (take the session and send request for you)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Tell Spring Security to never create or consult an HttpSession for authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    // Wire UserDetailService and SpringSecurityPasswordEncoder
    @Bean 
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
