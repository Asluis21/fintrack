package com.asluis.fintrack.fintrack.transaction;

import org.junit.jupiter.api.Test;

import com.asluis.fintrack.fintrack.user.model.Role;
import com.asluis.fintrack.fintrack.user.model.User;
import com.asluis.fintrack.fintrack.user.model.UserEmail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryTest {

    private User user() {
        return new User(new UserEmail("encrypted", "hash"), "hashedPassword", Role.USER);
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
