package com.semocompany.subscriptionservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semocompany.subscriptionservice.service.CategoryService;
import com.semocompany.subscriptionservice.web.dto.CategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private final String userId = "test-user-id";

    @Test
    @DisplayName("카테고리 생성 API 테스트")
    void createCategory() throws Exception {
        // given
        CategoryDto.CreateRequest request = new CategoryDto.CreateRequest();
        // Using reflection to set private field for test
        var field = request.getClass().getDeclaredField("name");
        field.setAccessible(true);
        field.set(request, "새 카테고리");

        given(categoryService.createCategory(any(), anyString()))
                .willReturn(CategoryDto.Response.builder().id(1L).name("새 카테고리").build());

        // when & then
        mockMvc.perform(post("/api/categories")
                        .header("X-USER-ID", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("새 카테고리"));
    }

    @Test
    @DisplayName("카테고리 목록 조회 API 테스트")
    void getUserCategories() throws Exception {
        // given
        List<CategoryDto.Response> responses = List.of(
                CategoryDto.Response.builder().id(1L).name("IT").build(),
                CategoryDto.Response.builder().id(2L).name("경제").build()
        );
        given(categoryService.getUserCategories(userId)).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/categories")
                        .header("X-USER-ID", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("IT"))
                .andExpect(jsonPath("$[1].name").value("경제"));
    }
}