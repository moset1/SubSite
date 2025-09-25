package com.semocompany.subscriptionservice.service;

import com.semocompany.subscriptionservice.domain.Category;
import com.semocompany.subscriptionservice.repository.CategoryRepository;
import com.semocompany.subscriptionservice.web.dto.CategoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private String userId;
    private Category category;

    @BeforeEach
    void setUp() {
        userId = "test-user-id";
        category = Category.builder()
                .name("테스트 카테고리")
                .userId(userId)
                .build();
    }

    @Test
    @DisplayName("카테고리 생성 테스트")
    void createCategory() {
        // given
        CategoryDto.CreateRequest request = new CategoryDto.CreateRequest();
        // Using reflection or a setter would be better if the class was designed for it.
        // For this test, we'll assume a way to set the name.
        try {
            var field = request.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(request, "새 카테고리");
        } catch (Exception e) {
            e.printStackTrace();
        }

        given(categoryRepository.save(any(Category.class))).willReturn(category);

        // when
        CategoryDto.Response response = categoryService.createCategory(request, userId);

        // then
        assertThat(response.getName()).isEqualTo("테스트 카테고리");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("사용자 ID로 카테고리 목록 조회 테스트")
    void getUserCategories() {
        // given
        given(categoryRepository.findByUserId(userId)).willReturn(List.of(category));

        // when
        List<CategoryDto.Response> responses = categoryService.getUserCategories(userId);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).isEqualTo("테스트 카테고리");
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void deleteCategory() {
        // given
        Long categoryId = 1L;
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));

        // when
        categoryService.deleteCategory(categoryId, userId);

        // then
        verify(categoryRepository).delete(category);
    }
}