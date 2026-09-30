package com.asluis.fintrack.fintrack.shared.security.infrastructure;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.asluis.fintrack.fintrack.common.exception.InvalidJWTException;
import com.asluis.fintrack.fintrack.shared.domain.JWTResponse;
import com.asluis.fintrack.fintrack.shared.domain.port.JWTService;
import com.asluis.fintrack.fintrack.shared.security.domain.JwtPrincipal;
import com.asluis.fintrack.fintrack.user.domain.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor 
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = "";
        
        if (request.getCookies() != null) {
            for(Cookie e : request.getCookies()){
                
                if (e.getName().equals("jwt-token")) {
                    token = e.getValue();
                }
            }
        }

        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try{

            JWTResponse claims = jwtService.verifyToken(token);
            JwtPrincipal principal = new JwtPrincipal(Long.valueOf(claims.subject()), Role.valueOf(claims.role()));
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + principal.role()));
            UsernamePasswordAuthenticationToken authUser = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authUser);
        } catch (InvalidJWTException e){
            // No throw error, just continue the flow
        }

        filterChain.doFilter(request, response);        
    }
    

}
