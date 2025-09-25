package com.semocompany.subscriptionservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semocompany.subscriptionservice.domain.SubscriptionType;
import com.semocompany.subscriptionservice.service.SubscriptionService;
import com.semocompany.subscriptionservice.web.dto.SubscriptionDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService subscriptionService;

    private final String userId = "test-user-id";

    @Test
    @DisplayName("구독 생성 API 테스트")
    void createSubscription() throws Exception {
        // given
        SubscriptionDto.CreateRequest request = new SubscriptionDto.CreateRequest();
        // Using reflection to set private fields for test
        var catIdField = request.getClass().getDeclaredField("categoryId");
        catIdField.setAccessible(true);
        catIdField.set(request, 1L);
        var urlField = request.getClass().getDeclaredField("url");
        urlField.setAccessible(true);
        urlField.set(request, "https://example.com");
        var keywordsField = request.getClass().getDeclaredField("keywords");
        keywordsField.setAccessible(true);
        keywordsField.set(request, List.of("test"));

        SubscriptionDto.Response response = SubscriptionDto.Response.builder()
                .id(1L)
                .categoryId(1L)
                .url("https://example.com")
                .keywords(List.of("test"))
                .type(SubscriptionType.CRAWL)
                .build();

        given(subscriptionService.createSubscription(any(), anyString())).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/subscriptions")
                        .header("X-USER-ID", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value("https://example.com"));
    }
}