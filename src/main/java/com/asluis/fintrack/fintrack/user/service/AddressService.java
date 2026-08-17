package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.user.dto.AddressRequest;
import com.asluis.fintrack.fintrack.user.dto.AddressResponse;

public interface AddressService {

    AddressResponse create(AddressRequest request);

    AddressResponse update(Long id, AddressRequest request);

    Optional<AddressResponse> findById(Long id);

    void deleteById(Long id);
}
