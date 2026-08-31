package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.user.dto.UserPreferencesRequest;
import com.asluis.fintrack.fintrack.user.dto.UserPreferencesResponse;

public interface UserPreferencesService {

    UserPreferencesResponse create(UserPreferencesRequest request);

    UserPreferencesResponse update(Long id, UserPreferencesRequest request);

    Optional<UserPreferencesResponse> findById(Long id);

    void deleteById(Long id);
}
