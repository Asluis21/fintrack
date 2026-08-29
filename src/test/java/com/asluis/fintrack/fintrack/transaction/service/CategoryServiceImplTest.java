package com.asluis.fintrack.fintrack.transaction.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.shared.exception.EntityNotFoundException;
import com.asluis.fintrack.fintrack.transaction.domain.AccountingType;
import com.asluis.fintrack.fintrack.transaction.domain.Category;
import com.asluis.fintrack.fintrack.transaction.domain.CategoryOwnerType;
import com.asluis.fintrack.fintrack.transaction.dto.CategoryRequest;
import com.asluis.fintrack.fintrack.transaction.dto.CategoryResponse;
import com.asluis.fintrack.fintrack.transaction.repository.CategoryRepository;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;
import com.asluis.fintrack.fintrack.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    private User user() {
        return User.builder()
                .email(new UserEmail("user@example.com", encryptionService, hashService))
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void create_systemCategory_doesNotResolveUser() {
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse response = categoryService.create(
                new CategoryRequest("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null));

        assertThat(response.categoryOwnerType()).isEqualTo(CategoryOwnerType.SYSTEM);
        assertThat(response.createdById()).isNull();
        verify(userRepository, never()).findById(any());
    }

    @Test
    void create_userCategory_resolvesCreator() {
        User user = user();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse response = categoryService.create(
                new CategoryRequest("Personal", AccountingType.EXPENSE, CategoryOwnerType.USER, 1L));

        assertThat(response.categoryOwnerType()).isEqualTo(CategoryOwnerType.USER);
    }

    @Test
    void create_userCategory_creatorNotFound_throws() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.create(
                new CategoryRequest("Personal", AccountingType.EXPENSE, CategoryOwnerType.USER, 1L)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(categoryService.findById(1L)).isEmpty();
    }

    @Test
    void update_notFound_throws() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.update(1L,
                new CategoryRequest("Food", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update_renamesAndSaves() {
        Category category = new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryResponse response = categoryService.update(1L,
                new CategoryRequest("Food", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null));

        assertThat(response.name()).isEqualTo("Food");
    }

    @Test
    void deleteById_notFound_throwsAndNeverDeletes() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.deleteById(1L)).isInstanceOf(EntityNotFoundException.class);
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void deleteById_found_deletes() {
        Category category = new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteById(1L);

        verify(categoryRepository).delete(category);
    }
}
