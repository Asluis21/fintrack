package com.asluis.fintrack.fintrack.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asluis.fintrack.fintrack.user.domain.PersonalData;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {

    Optional<PersonalData> findByUser_Id(Long userId);
}