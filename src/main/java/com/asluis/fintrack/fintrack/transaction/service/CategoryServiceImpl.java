package com.asluis.fintrack.fintrack.transaction.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.transaction.domain.Category;
import com.asluis.fintrack.fintrack.transaction.dto.CategoryMapper;
import com.asluis.fintrack.fintrack.transaction.dto.CategoryRequest;
import com.asluis.fintrack.fintrack.transaction.dto.CategoryResponse;
import com.asluis.fintrack.fintrack.transaction.repository.CategoryRepository;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CategoryResponse create(CategoryRequest request) {

        User createdBy = null;

        if (request.createdById() != null) {
            createdBy = userRepository.findById(request.createdById())
                .orElseThrow(() -> new EntityNotFoundException(User.class, request.createdById()));
        }

        Category categoryEntity = CategoryMapper.toEntity(request, createdBy);

        return CategoryMapper.toResponse(categoryRepository.save(categoryEntity));
    }

    @Transactional
    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category foundCategory = findCategory(id);

        foundCategory.rename(request.name());

        return CategoryMapper.toResponse(categoryRepository.save(foundCategory));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CategoryResponse> findById(Long id) {
        return categoryRepository.findById(id).map(CategoryMapper::toResponse);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        categoryRepository.delete(findCategory(id));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Category.class, id));
    }
}