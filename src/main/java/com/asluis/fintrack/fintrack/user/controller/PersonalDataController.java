package com.asluis.fintrack.fintrack.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asluis.fintrack.fintrack.user.dto.PersonalDataRequest;
import com.asluis.fintrack.fintrack.user.dto.PersonalDataResponse;
import com.asluis.fintrack.fintrack.user.service.PersonalDataService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/personal-data")
public class PersonalDataController {

    private final PersonalDataService personalDataService;

    @PostMapping
    public ResponseEntity<PersonalDataResponse> create(@Valid @RequestBody PersonalDataRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personalDataService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalDataResponse> update(@PathVariable Long id, @Valid @RequestBody PersonalDataRequest request) {
        return ResponseEntity.ok(personalDataService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalDataResponse> findById(@PathVariable Long id) {
        return personalDataService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personalDataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
