package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequest request) {
        UserEmail userEmail = new UserEmail(request.emailEncrypted(), request.emailLookupHash());
        return new User(userEmail, request.passwordHash(), request.role());
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
