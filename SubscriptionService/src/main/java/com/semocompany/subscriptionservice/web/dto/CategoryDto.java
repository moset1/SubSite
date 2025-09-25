package com.semocompany.subscriptionservice.web.dto;

import com.semocompany.subscriptionservice.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Category name cannot be blank")
        @Size(max = 50, message = "Category name must be less than 50 characters")
        private String name;
    }

    @Getter
    public static class Response {
        private final Long id;
        private final String name;

        @Builder
        public Response(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Response from(Category category) {
            return Response.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        }

        public static List<Response> from(List<Category> categories) {
            return categories.stream()
                    .map(Response::from)
                    .collect(Collectors.toList());
        }
    }
}