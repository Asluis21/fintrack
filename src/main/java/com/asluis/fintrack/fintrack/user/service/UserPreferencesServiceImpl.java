package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserPreferences;
import com.asluis.fintrack.fintrack.user.dto.UserPreferencesMapper;
import com.asluis.fintrack.fintrack.user.dto.UserPreferencesRequest;
import com.asluis.fintrack.fintrack.user.dto.UserPreferencesResponse;
import com.asluis.fintrack.fintrack.user.repository.UserPreferencesRepository;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserPreferencesServiceImpl implements UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserPreferencesResponse create(UserPreferencesRequest request) {

        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new EntityNotFoundException(User.class, request.userId()));

        UserPreferences userPreferencesEntity = UserPreferencesMapper.toEntity(request, user);

        return UserPreferencesMapper.toResponse(userPreferencesRepository.save(userPreferencesEntity));
    }

    @Transactional
    @Override
    public UserPreferencesResponse update(Long id, UserPreferencesRequest request) {

        UserPreferences foundUserPreferences = findUserPreferences(id);

        if (request.darkMode()) {
            foundUserPreferences.changeToDarkMode();
        } else {
            foundUserPreferences.changeToLightMode();
        }

        foundUserPreferences.changeLanguageCode(request.languageCode());

        return UserPreferencesMapper.toResponse(userPreferencesRepository.save(foundUserPreferences));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserPreferencesResponse> findById(Long id) {
        return userPreferencesRepository.findById(id).map(UserPreferencesMapper::toResponse);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userPreferencesRepository.delete(findUserPreferences(id));
    }

    private UserPreferences findUserPreferences(Long id) {
        return userPreferencesRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(UserPreferences.class, id));
    }
}