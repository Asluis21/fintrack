package com.asluis.fintrack.fintrack.transaction.domain;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.shared.domain.port.EncryptionService;
import com.asluis.fintrack.fintrack.shared.domain.port.HashService;
import com.asluis.fintrack.fintrack.user.domain.Role;
import com.asluis.fintrack.fintrack.user.domain.User;
import com.asluis.fintrack.fintrack.user.domain.UserEmail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryTest {

    private final EncryptionService encryptionService = mock(EncryptionService.class);
    private final HashService hashService = mock(HashService.class);

    CategoryTest() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        when(hashService.hash(anyString())).thenReturn("hash");
    }

    private User user() {
        return User.builder()
                .email(new UserEmail("user@example.com", encryptionService, hashService))
                .passwordHash("hashedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void systemCategory_withCreator_throws() {
        assertThatThrownBy(() -> new Category("Groceries", AccountingType.EXPENSE,
                CategoryOwnerType.SYSTEM, user()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("System categories cannot have a creator");
    }

    @Test
    void userCategory_withoutCreator_throws() {
        assertThatThrownBy(() -> new Category("Groceries", AccountingType.EXPENSE,
                CategoryOwnerType.USER, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User categories must have a creator");
    }

    @Test
    void systemCategory_withoutCreator_isCreated() {
        Category category = new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null);

        assertThat(category.getCategoryOwnerType()).isEqualTo(CategoryOwnerType.SYSTEM);
    }

    @Test
    void isAvailableFor_systemCategory_isAvailableForAnyUser() {
        Category category = new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null);

        assertThat(category.isAvailableFor(user())).isTrue();
    }

    @Test
    void isAvailableFor_userCategory_onlyAvailableForOwner() {
        User owner = user();
        User other = user();
        Category category = new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.USER, owner);

        assertThat(category.isAvailableFor(owner)).isTrue();
        assertThat(category.isAvailableFor(other)).isFalse();
    }

    @Test
    void rename_updatesName() {
        Category category = new Category("Groceries", AccountingType.EXPENSE, CategoryOwnerType.SYSTEM, null);

        category.rename("Food");

        assertThat(category.getName()).isEqualTo("Food");
    }
}
