package com.semocompany.subscriptionservice.domain.subscription.service;

import com.semocompany.subscriptionservice.domain.category.entity.Category;
import com.semocompany.subscriptionservice.domain.category.repository.CategoryRepository;
import com.semocompany.subscriptionservice.domain.subscription.dto.SubscriptionDTO;
import com.semocompany.subscriptionservice.domain.subscription.entity.Subscription;
import com.semocompany.subscriptionservice.domain.subscription.entity.SubscriptionType;
import com.semocompany.subscriptionservice.domain.subscription.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private UUID userId;
    private UUID subscriptionId;
    private Subscription subscription;
    private Category category;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        subscriptionId = UUID.randomUUID();

        category = Category.builder()
                .name("test-category")
                .userId(userId)
                .build();
        ReflectionTestUtils.setField(category, "id", UUID.randomUUID());


        subscription = Subscription.builder()
                .category(category)
                .url("https://example.com")
                .keywords(List.of("old-keyword"))
                .type(SubscriptionType.CRAWL)
                .postLinkSelector(".old-post")
                .nextPageSelector(".old-next")
                .build();
        ReflectionTestUtils.setField(subscription, "id", subscriptionId);
    }

    @Test
    @DisplayName("구독 정보 업데이트 시 CSS 선택자도 함께 변경되어야 한다")
    void updateSubscription_withCssSelectors_shouldUpdateSelectors() {
        // given
        List<String> newKeywords = List.of("new-keyword");
        String newPostLinkSelector = ".new-post";
        String newNextPageSelector = ".new-next";

        // DTO를 생성자나 빌더로 직접 생성
        SubscriptionDTO.UpdateRequest updateRequest = new SubscriptionDTO.UpdateRequest(
                newKeywords,
                newPostLinkSelector,
                newNextPageSelector
        );

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

        // when
        SubscriptionDTO.Response response = subscriptionService.updateSubscription(subscriptionId, updateRequest, userId);

        // then
        assertThat(response.getKeywords()).isEqualTo(newKeywords);
        assertThat(response.getPostLinkSelector()).isEqualTo(newPostLinkSelector);
        assertThat(response.getNextPageSelector()).isEqualTo(newNextPageSelector);

        assertThat(subscription.getKeywords()).isEqualTo(newKeywords);
        assertThat(subscription.getPostLinkSelector()).isEqualTo(newPostLinkSelector);
        assertThat(subscription.getNextPageSelector()).isEqualTo(newNextPageSelector);
    }
}
