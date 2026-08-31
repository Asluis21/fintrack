package com.asluis.fintrack.fintrack.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asluis.fintrack.fintrack.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail_EmailLookupHash(String emailLookupHash);
}