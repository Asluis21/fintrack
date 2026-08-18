package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.dto.UserMapper;
import com.asluis.fintrack.fintrack.user.dto.UserRequest;
import com.asluis.fintrack.fintrack.user.dto.UserResponse;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final HashService hashService;

    @Override
    public UserResponse create(UserRequest request) {

        User userEntity = UserMapper.toEntity(request, encryptionService, hashService);

        return UserMapper.toResponse(userRepository.save(userEntity));
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {

        User foundUser = findUser(id);

        foundUser.changeEmail(request.email(), encryptionService, hashService);
        foundUser.changePassword(request.passwordHash());

        if (request.role() == Role.ADMIN) {
            foundUser.changeToAdmin();
        } else {
            foundUser.changeToUser();
        }

        return UserMapper.toResponse(userRepository.save(foundUser));
    }

    @Override
    public Optional<UserResponse> findById(Long id) {
        return userRepository.findById(id).map(UserMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.delete(findUser(id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }
}