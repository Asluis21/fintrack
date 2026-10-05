package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.shared.dto.RegisterRequest;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.dto.UserRequest;
import com.asluis.fintrack.fintrack.user.dto.UserResponse;

public interface UserService {

    UserResponse create(UserRequest request);

    User register(RegisterRequest request);

    UserResponse update(Long id, UserRequest request);

    Optional<UserResponse> findById(Long id);

    void deleteById(Long id);
}
