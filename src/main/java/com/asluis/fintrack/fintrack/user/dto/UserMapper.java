package com.asluis.fintrack.fintrack.user.dto;

import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserFactory;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequest request, UserFactory userFactory) {

        return userFactory
            .builder(request.email(), request.password())
            .role(request.role())
            .build();
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
