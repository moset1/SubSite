package com.semocompany.subscriptionservice.domain.category.service;

import com.semocompany.subscriptionservice.domain.category.dto.CategoryDTO;
import com.semocompany.subscriptionservice.domain.category.entity.Category;
import com.semocompany.subscriptionservice.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;


    @Transactional
    public CategoryDTO.Response createCategory(CategoryDTO.CreateRequest request, Long userId) {

        Category category = Category.builder()
                .name(request.getName())
                .userId(userId)
                .build();
        Category savedCategory = categoryRepository.save(category);
        return CategoryDTO.Response.from(savedCategory);
    }

    public List<CategoryDTO.Response> getUserCategories(String userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);
        return CategoryDTO.Response.from(categories);
    }

    @Transactional
    public void deleteCategory(Long categoryId, String userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다." + categoryId));

        if (!category.getUserId().equals(userId)) {
            throw new SecurityException("사용자가 해당 카테고리에 대한 권한이 없습니다.");
        }

        categoryRepository.delete(category);
    }
}
