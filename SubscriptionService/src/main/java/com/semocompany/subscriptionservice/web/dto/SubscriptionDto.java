package com.semocompany.subscriptionservice.web.dto;

import com.semocompany.subscriptionservice.domain.Subscription;
import com.semocompany.subscriptionservice.domain.SubscriptionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "Category ID cannot be null")
        private Long categoryId;

        @NotBlank(message = "URL cannot be blank")
        @URL(message = "Must be a valid URL")
        private String url;

        @NotNull
        @Size(min = 1, message = "At least one keyword is required")
        private List<String> keywords;

        // Defaulting to CRAWL for now, can be extended later
        private SubscriptionType type = SubscriptionType.CRAWL;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRequest {
        @NotNull
        @Size(min = 1, message = "At least one keyword is required")
        private List<String> keywords;
    }

    @Getter
    public static class Response {
        private final Long id;
        private final Long categoryId;
        private final String url;
        private final List<String> keywords;
        private final SubscriptionType type;

        @Builder
        public Response(Long id, Long categoryId, String url, List<String> keywords, SubscriptionType type) {
            this.id = id;
            this.categoryId = categoryId;
            this.url = url;
            this.keywords = keywords;
            this.type = type;
        }

        public static Response from(Subscription subscription) {
            return Response.builder()
                    .id(subscription.getId())
                    .categoryId(subscription.getCategory().getId())
                    .url(subscription.getUrl())
                    .keywords(subscription.getKeywords())
                    .type(subscription.getType())
                    .build();
        }

        public static List<Response> from(List<Subscription> subscriptions) {
            return subscriptions.stream()
                    .map(Response::from)
                    .collect(Collectors.toList());
        }
    }
}