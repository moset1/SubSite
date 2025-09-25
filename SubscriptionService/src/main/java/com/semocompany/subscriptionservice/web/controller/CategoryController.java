package com.semocompany.subscriptionservice.web.controller;

import com.semocompany.subscriptionservice.service.CategoryService;
import com.semocompany.subscriptionservice.web.dto.CategoryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto.Response> createCategory(
            @Valid @RequestBody CategoryDto.CreateRequest request,
            @RequestHeader("X-USER-ID") String userId) {
        CategoryDto.Response response = categoryService.createCategory(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto.Response>> getUserCategories(
            @RequestHeader("X-USER-ID") String userId) {
        List<CategoryDto.Response> responses = categoryService.getUserCategories(userId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId,
            @RequestHeader("X-USER-ID") String userId) {
        categoryService.deleteCategory(categoryId, userId);
        return ResponseEntity.noContent().build();
    }
}