package com.asluis.fintrack.fintrack.transaction.service;

import java.util.Optional;

import com.asluis.fintrack.fintrack.transaction.dto.CategoryRequest;
import com.asluis.fintrack.fintrack.transaction.dto.CategoryResponse;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    Optional<CategoryResponse> findById(Long id);

    void deleteById(Long id);
}
