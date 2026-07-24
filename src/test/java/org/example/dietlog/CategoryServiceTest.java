package org.example.dietlog;

import org.example.dietlog.domain.Category;
import org.example.dietlog.domain.MealType;
import org.example.dietlog.domain.User;
import org.example.dietlog.repository.CategoryRepository;
import org.example.dietlog.repository.UserRepository;
import org.example.dietlog.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;


@SpringBootTest
@Transactional
class CategoryServiceTest{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void 카테고리_CRUD_동작_확인() {

        User user = userRepository.save(
                User.builder()
                        .email("test@dietlog.com")
                        .password("encoded_password")
                        .nickname("테스터")
                        .build()
        );

        Category category = categoryService.createCategory(user, "점심 한식", MealType.LUNCH, "한식");

        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo("점심 한식");

        List<Category> categories = categoryService.getMyCategories(user);

        assertThat(categories).hasSize(1);

        Category updated = categoryService.updateCategory(user, category.getId(), "점심 분식", MealType.LUNCH, "분식");

        assertThat(updated.getName()).isEqualTo("점심 분식");
        assertThat(updated.getFoodGroup()).isEqualTo("분식");

        categoryService.deleteCategory(user, category.getId());

        assertThat(categoryService.getMyCategories(user)).isEmpty();

    }

}
