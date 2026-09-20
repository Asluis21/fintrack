package com.asluis.fintrack.fintrack.shared.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.security.domain.SecurityUser;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserDetailService implements UserDetailsService{
    
    private UserRepository userRepository;

    private HashService hashService;

    @Override
    public UserDetails loadUserByUsername(String submittedEmail) throws UsernameNotFoundException {
        
        String hashEmail = hashService.hash(submittedEmail);

        User user = userRepository.findByUserEmail_EmailLookupHash(hashEmail)
            .orElseThrow(() -> new UsernameNotFoundException(submittedEmail + " is incorrect"));

        return new SecurityUser(user, submittedEmail);
    }

    
}
