package com.semocompany.subscriptionservice.service;

import com.semocompany.subscriptionservice.domain.Category;
import com.semocompany.subscriptionservice.repository.CategoryRepository;
import com.semocompany.subscriptionservice.web.dto.CategoryDto;
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
    public CategoryDto.Response createCategory(CategoryDto.CreateRequest request, String userId) {
        // TODO: Check for duplicate category name for the same user
        Category category = Category.builder()
                .name(request.getName())
                .userId(userId)
                .build();
        Category savedCategory = categoryRepository.save(category);
        return CategoryDto.Response.from(savedCategory);
    }

    public List<CategoryDto.Response> getUserCategories(String userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);
        return CategoryDto.Response.from(categories);
    }

    @Transactional
    public void deleteCategory(Long categoryId, String userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        if (!category.getUserId().equals(userId)) {
            throw new SecurityException("User does not have permission to delete this category");
        }

        // TODO: Handle subscriptions in this category. Maybe move them to a default category or delete them.
        categoryRepository.delete(category);
    }
}