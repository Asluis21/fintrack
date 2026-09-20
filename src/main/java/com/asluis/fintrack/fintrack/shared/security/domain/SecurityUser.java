package com.asluis.fintrack.fintrack.shared.security.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.asluis.fintrack.fintrack.user.domain.Status;
import com.asluis.fintrack.fintrack.user.domain.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SecurityUser implements UserDetails{

    private User user;
    private String submittedEmail;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return submittedEmail;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == Status.ACTIVE;
    }

    public User getUser() {
        return user;
    }
    
}
