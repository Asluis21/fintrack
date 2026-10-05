package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.dto.RegisterRequest;
import com.asluis.fintrack.fintrack.shared.exception.EmailAlreadyRegisteredException;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserFactory;
import com.asluis.fintrack.fintrack.user.dto.UserMapper;
import com.asluis.fintrack.fintrack.user.dto.UserRequest;
import com.asluis.fintrack.fintrack.user.dto.UserResponse;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static com.asluis.fintrack.fintrack.common.validation.ValidationUtils.normalizeRequiredText;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final HashService hashService;

    @Transactional
    @Override
    public UserResponse create(UserRequest request) {

        User userEntity = UserMapper.toEntity(request, userFactory);

        return UserMapper.toResponse(userRepository.save(userEntity));
    }

    @Transactional
    @Override
    public User register(RegisterRequest request) {

        String emailLookupHash = hashService.hash(normalizeRequiredText(request.email(), "Email"));

        if (userRepository.findByUserEmail_EmailLookupHash(emailLookupHash).isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }

        User userEntity = userFactory
            .builder(request.email(), request.password())
            .role(Role.USER)
            .build();

        return userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public UserResponse update(Long id, UserRequest request) {

        User foundUser = findUser(id);

        userFactory.changeEmail(foundUser, request.email());
        foundUser.changePassword(userFactory.hashPassword(request.password()));

        if (request.role() == Role.ADMIN) {
            foundUser.changeToAdmin();
        } else {
            foundUser.changeToUser();
        }

        return UserMapper.toResponse(userRepository.save(foundUser));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserResponse> findById(Long id) {
        return userRepository.findById(id).map(UserMapper::toResponse);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.delete(findUser(id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }
}