package com.asluis.fintrack.fintrack.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asluis.fintrack.fintrack.transaction.Category;
import com.asluis.fintrack.fintrack.transaction.CategoryOwnerType;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCategoryOwnerType(CategoryOwnerType categoryOwnerType);

    List<Category> findByCreatedBy_Id(Long userId);
}