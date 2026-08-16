package com.asluis.fintrack.fintrack.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asluis.fintrack.fintrack.user.model.UserPreferences;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

    Optional<UserPreferences> findByUser_Id(Long userId);
}