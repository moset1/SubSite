package com.semocompany.subscriptionservice.api;


import com.semocompany.subscriptionservice.domain.category.dto.CategoryDTO;
import com.semocompany.subscriptionservice.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO .Response> createCategory(
            @Valid @RequestBody CategoryDTO.CreateRequest request,
            @RequestHeader("X-USER-ID") UUID userId) {
        CategoryDTO.Response response = categoryService.createCategory(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO.Response>> getUserCategories(
            @RequestHeader("X-USER-ID") UUID userId) {
        List<CategoryDTO.Response> responses = categoryService.getUserCategories(userId);
        return ResponseEntity.ok(responses);
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
        @PathVariable UUID categoryId,
        @RequestHeader("X-USER-ID") UUID userId) {

        categoryService.deleteCategory(categoryId, userId);
        return ResponseEntity.noContent().build();
    }
}