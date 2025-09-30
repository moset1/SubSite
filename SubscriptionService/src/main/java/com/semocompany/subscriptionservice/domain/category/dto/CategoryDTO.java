package com.semocompany.subscriptionservice.domain.category.dto;

import com.semocompany.subscriptionservice.domain.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryDTO {

    // static으로 선언한 이유는 메모리 누수 방지를 위해
    @Getter
    @NoArgsConstructor
    public static class CreateRequest {

        @NotBlank(message = "카테고리는 빈 값이면 안됩니다.")
        @Size(max = 50, message = "카테고리 이름은 50자 이내여야 합니다.")
        private String name;
    }

    @Getter
    public static class Response {
        private final UUID id;
        private final String name;

        @Builder
        public Response(UUID id, String name) {
            this.id = id;
            this.name = name;
        }

        // DTO로의 변환을 DTO 클래스에게 위임하기
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
