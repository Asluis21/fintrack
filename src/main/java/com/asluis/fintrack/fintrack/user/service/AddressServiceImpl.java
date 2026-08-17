package com.asluis.fintrack.fintrack.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.user.domain.Address;
import com.asluis.fintrack.fintrack.user.domain.Country;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.dto.AddressMapper;
import com.asluis.fintrack.fintrack.user.dto.AddressRequest;
import com.asluis.fintrack.fintrack.user.dto.AddressResponse;
import com.asluis.fintrack.fintrack.user.repository.AddressRepository;
import com.asluis.fintrack.fintrack.user.repository.CountryRepository;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    

    @Override
    public AddressResponse create(AddressRequest request) {

        Country country = countryRepository.findById(request.countryId())
            .orElseThrow(() -> new EntityNotFoundException(Country.class, request.countryId()));
        
        User user = userRepository.findById(request.countryId())
            .orElseThrow(() -> new EntityNotFoundException(User.class, request.countryId()));

        Address addressEntity = AddressMapper.toEntity(request, country, user);

        return AddressMapper.toResponse(addressRepository.save(addressEntity));
    }

    @Override
    public AddressResponse update(Long id, AddressRequest request) {
        
        Address founAddress = findAddress(id);

        founAddress.changeAddressName(request.name());
        founAddress.changeCity(request.city());
        founAddress.changePostalCode(request.postalCode());
        founAddress.changeStreet(request.street());
        
        return AddressMapper.toResponse(addressRepository.save(founAddress));
    }

    @Override
    public Optional<AddressResponse> findById(Long id) {
        return addressRepository.findById(id).map(AddressMapper::toResponse);
    }

    @Override
    public void deleteById(Long id) {
        
        Address foundAddress = findAddress(id);
        addressRepository.delete(foundAddress);
    }

    private Address findAddress(Long id){

        return addressRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Address.class, id));
    }

}
