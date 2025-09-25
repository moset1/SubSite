package com.semocompany.subscriptionservice.service;

import com.semocompany.subscriptionservice.repository.CategoryRepository;
import com.semocompany.subscriptionservice.repository.SubscriptionRepository;
import com.semocompany.subscriptionservice.web.dto.CategoryDto;
import com.semocompany.subscriptionservice.web.dto.SubscriptionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // Rollback after each test
class SubscriptionFlowIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    // We don't want to connect to a real RabbitMQ instance for this test
    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("카테고리 생성 후 구독 추가 통합 테스트")
    void createCategoryAndSubscriptionFlow() {
        // given
        String userId = "integration-test-user";

        // 1. Create a category
        CategoryDto.CreateRequest categoryRequest = new CategoryDto.CreateRequest();
        try {
            var field = categoryRequest.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(categoryRequest, "기술 블로그");
        } catch (Exception e) { e.printStackTrace(); }

        CategoryDto.Response categoryResponse = categoryService.createCategory(categoryRequest, userId);
        Long categoryId = categoryResponse.getId();

        // 2. Create a subscription in that category
        SubscriptionDto.CreateRequest subscriptionRequest = new SubscriptionDto.CreateRequest();
         try {
            var catIdField = subscriptionRequest.getClass().getDeclaredField("categoryId");
            catIdField.setAccessible(true);
            catIdField.set(subscriptionRequest, categoryId);

            var urlField = subscriptionRequest.getClass().getDeclaredField("url");
            urlField.setAccessible(true);
            urlField.set(subscriptionRequest, "https://tech.example.com");

            var keywordsField = subscriptionRequest.getClass().getDeclaredField("keywords");
            keywordsField.setAccessible(true);
            keywordsField.set(subscriptionRequest, List.of("kubernetes", "docker"));
        } catch (Exception e) { e.printStackTrace(); }

        // when
        subscriptionService.createSubscription(subscriptionRequest, userId);

        // then
        assertThat(categoryRepository.count()).isGreaterThan(0);
        assertThat(subscriptionRepository.count()).isEqualTo(1);

        var savedSubscription = subscriptionRepository.findAll().get(0);
        assertThat(savedSubscription.getCategory().getId()).isEqualTo(categoryId);
        assertThat(savedSubscription.getUrl()).isEqualTo("https://tech.example.com");
    }
}