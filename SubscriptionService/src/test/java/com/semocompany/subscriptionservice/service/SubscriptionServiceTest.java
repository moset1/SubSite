package com.semocompany.subscriptionservice.service;

import com.semocompany.subscriptionservice.domain.Category;
import com.semocompany.subscriptionservice.domain.Subscription;
import com.semocompany.subscriptionservice.domain.SubscriptionType;
import com.semocompany.subscriptionservice.repository.CategoryRepository;
import com.semocompany.subscriptionservice.repository.SubscriptionRepository;
import com.semocompany.subscriptionservice.scheduler.CrawlingTaskPublisher;
import com.semocompany.subscriptionservice.web.dto.SubscriptionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CrawlingTaskPublisher publisher;

    private String userId;
    private Category category;
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        userId = "test-user-id";
        category = Category.builder().name("IT").userId(userId).build();
        subscription = Subscription.builder()
                .category(category)
                .url("https://example.com")
                .keywords(List.of("java", "spring"))
                .type(SubscriptionType.CRAWL)
                .build();
    }

    @Test
    @DisplayName("구독 생성 테스트")
    void createSubscription() {
        // given
        SubscriptionDto.CreateRequest request = new SubscriptionDto.CreateRequest();
        // Set request fields using reflection or setters
        // This is a workaround for the test, not ideal for production code.
        try {
            var catIdField = request.getClass().getDeclaredField("categoryId");
            catIdField.setAccessible(true);
            catIdField.set(request, 1L);

            var urlField = request.getClass().getDeclaredField("url");
            urlField.setAccessible(true);
            urlField.set(request, "https://example.com");

            var keywordsField = request.getClass().getDeclaredField("keywords");
            keywordsField.setAccessible(true);
            keywordsField.set(request, List.of("java", "spring"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(subscriptionRepository.save(any(Subscription.class))).willReturn(subscription);

        // when
        SubscriptionDto.Response response = subscriptionService.createSubscription(request, userId);

        // then
        assertThat(response.getUrl()).isEqualTo("https://example.com");
        assertThat(response.getKeywords()).contains("java", "spring");
    }

    @Test
    @DisplayName("사용자 구독 목록 조회 테스트")
    void getUserSubscriptions() {
        // given
        given(subscriptionRepository.findByCategory_UserId(userId)).willReturn(List.of(subscription));

        // when
        List<SubscriptionDto.Response> responses = subscriptionService.getUserSubscriptions(userId);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getUrl()).isEqualTo("https://example.com");
    }

    @Test
    @DisplayName("수동 업데이트 확인 테스트")
    void checkUpdatesForUser() {
        // given
        given(subscriptionRepository.findByCategory_UserId(userId)).willReturn(List.of(subscription));

        // when
        subscriptionService.checkUpdatesForUser(userId);

        // then
        verify(publisher, times(1)).publish(any(com.semocompany.subscriptionservice.scheduler.CrawlingTask.class));
    }
}