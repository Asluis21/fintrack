package com.asluis.fintrack.fintrack.transaction.dto;

import com.asluis.fintrack.fintrack.transaction.domain.Category;
import com.asluis.fintrack.fintrack.user.domain.User;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toEntity(CategoryRequest request, User createdBy) {
        return new Category(request.name(), request.accountingType(), request.categoryOwnerType(), createdBy);
    }

    public static CategoryResponse toResponse(Category category) {
        Long createdById = category.getCreatedBy() != null ? category.getCreatedBy().getId() : null;
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getAccountingType(),
                category.getCategoryOwnerType(),
                createdById,
                category.getCreatedAt()
        );
    }
}
