package com.asluis.fintrack.fintrack.shared.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.security.domain.SecurityUser;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserDetailService implements UserDetailsService{
    
    private UserRepository userRepository;

    private HashService hashService;

    @Override
    public UserDetails loadUserByUsername(String submittedEmail) throws UsernameNotFoundException {
        
        if (submittedEmail == null || submittedEmail.isBlank()) {
            throw new UsernameNotFoundException("Email is required");
        }

        // Normalize the same way UserEmail does on registration, so the lookup hashes match
        String hashEmail = hashService.hash(normalizeRequiredText(submittedEmail, "Email"));

        User user = userRepository.findByUserEmail_EmailLookupHash(hashEmail)
            .orElseThrow(() -> new UsernameNotFoundException(submittedEmail + " is incorrect"));

        return new SecurityUser(user, submittedEmail);
    }

    
}
