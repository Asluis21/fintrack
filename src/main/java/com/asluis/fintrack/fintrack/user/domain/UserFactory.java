package com.asluis.fintrack.fintrack.user.domain;

import org.springframework.stereotype.Component;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.domain.port.PasswordHasher;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UserFactory {

    private final EncryptionService encryptionService;
    private final HashService hashService;
    private final PasswordHasher passwordHasher;

    public User.UserBuilder builder(String email, String password){
        UserEmail userEmail = new UserEmail(email, encryptionService, hashService);

        return User.builder()
                .email(userEmail)
                .passwordHash(passwordHasher.hash(password));
    }

    public void changeEmail(User user, String email) {
        user.changeEmail(new UserEmail(email, encryptionService, hashService));
    }

    public String hashPassword(String password) {
        return passwordHasher.hash(password);
    }
}
