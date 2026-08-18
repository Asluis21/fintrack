package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.user.domain.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequest request, EncryptionService encryptionService, HashService hashService) {
        return new User(request.email(), request.passwordHash(), request.role(), encryptionService, hashService);
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
