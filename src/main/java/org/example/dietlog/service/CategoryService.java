package org.example.dietlog.service;

import lombok.RequiredArgsConstructor;
import org.example.dietlog.domain.Category;
import org.example.dietlog.domain.MealType;
import org.example.dietlog.domain.User;
import org.example.dietlog.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getMyCategories(User user) {
        return categoryRepository.findByUser(user);
    }

    @Transactional
    public Category createCategory(User user, String name, MealType mealType, String foodGroup) {
        Category category = Category.builder()
                .user(user)
                .name(name)
                .mealType(mealType)
                .foodGroup(foodGroup)
                .build();
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(User user, Long categoryId, String name, MealType mealType, String foodGroup) {

        Category category = getMyCategoryOrThrow(user, categoryId);
        category.update(name, mealType, foodGroup);
        return category;
    }

    @Transactional
    public void deleteCategory(User user, Long categoryId) {

        Category category = getMyCategoryOrThrow(user, categoryId);
        categoryRepository.delete(category);
    }

    private Category getMyCategoryOrThrow(User user, Long categoryId) {
        return categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new RuntimeException("등록되지않은 categoryId 입니다. " + categoryId));
    }
}
