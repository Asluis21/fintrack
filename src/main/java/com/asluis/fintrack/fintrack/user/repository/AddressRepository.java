package com.asluis.fintrack.fintrack.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asluis.fintrack.fintrack.user.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUser_Id(Long userId);
}