package com.semocompany.subscriptionservice.domain.subscription.dto;

import com.semocompany.subscriptionservice.domain.subscription.entity.Subscription;
import com.semocompany.subscriptionservice.domain.subscription.entity.SubscriptionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubscriptionDTO {

    @Getter
    @NoArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "Category 아이디는 null일 수 없습니다.")
        private UUID categoryId;

        @NotBlank(message = "URL은 빈 값이면 안됩니다.")
        @URL(message = "유효한 URL을 입력해야합니다.")
        private String url;

        @NotNull
        @Size(min = 1, message = "최소 하나의 키워드가 필요합니다.")
        private List<String> keywords;

        private String postLinkSelector;
        private String nextPageSelector;

        private final SubscriptionType type = SubscriptionType.CRAWL;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRequest {
        @NotNull
        @Size(min = 1, message = "최소 하나의 키워드가 필요합니다.")
        private List<String> keywords;
        private String postLinkSelector;
        private String nextPageSelector;

        @Builder
        public UpdateRequest(List<String> newKeywords, String newPostLinkSelector, String newNextPageSelector) {
            this.keywords = newKeywords;
            this.postLinkSelector = newPostLinkSelector;
            this.nextPageSelector = newNextPageSelector;
        }
    }


    @Getter
    public static class Response {
        private final UUID id;
        private final UUID categoryId;
        private final String url;
        private final List<String> keywords;
        private final SubscriptionType type;
        private final String postLinkSelector;
        private final String nextPageSelector;


        @Builder
        public Response(UUID id, UUID categoryId, String url, List<String> keywords, SubscriptionType type, String postLinkSelector, String nextPageSelector) {
            this.id = id;
            this.categoryId = categoryId;
            this.url = url;
            this.keywords = keywords;
            this.type = type;
            this.postLinkSelector = postLinkSelector;
            this.nextPageSelector = nextPageSelector;
        }

        public static Response from(Subscription subscription) {
            return Response.builder()
                    .id(subscription.getId())
                    .categoryId(subscription.getCategory().getId())
                    .url(subscription.getUrl())
                    .keywords(subscription.getKeywords())
                    .type(subscription.getType())
                    .postLinkSelector(subscription.getPostLinkSelector())
                    .nextPageSelector(subscription.getNextPageSelector())
                    .build();
        }

        public static List<Response> from(List<Subscription> subscriptions) {
            return subscriptions.stream()
                    .map(Response::from)
                    .collect(Collectors.toList());
        }
    }
}
