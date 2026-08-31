package com.asluis.fintrack.fintrack.shared.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.security.domain.SecurityUser;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserDetailService {
    
    private UserRepository userRepository;

    private HashService hashService;

    public UserDetails loadUserByEmail(String submittedEmail){

        String hashEmail = hashService.hash(submittedEmail);

        User user = userRepository.findByUserEmail_EmailLookupHash(hashService.hash(submittedEmail))
            .orElseThrow(() -> new UsernameNotFoundException(submittedEmail + " is incorrect"));

        return new SecurityUser(user, hashEmail);
    }

}
