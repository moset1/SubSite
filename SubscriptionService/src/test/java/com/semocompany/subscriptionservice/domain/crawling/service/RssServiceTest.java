package com.semocompany.subscriptionservice.domain.crawling.service;

import com.semocompany.subscriptionservice.domain.crawling.worker.Article;
import com.semocompany.subscriptionservice.domain.crawling.worker.RssParser;
import com.semocompany.subscriptionservice.domain.notification.repository.NotificationRepository;
import com.semocompany.subscriptionservice.domain.subscription.entity.Subscription;
import com.semocompany.subscriptionservice.domain.subscription.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RssServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private RssParser rssParser;

    @InjectMocks
    private RssService rssService;

    @Test
    void processRssSubscription_shouldSaveNotification_whenKeywordIsFound() {
        // given
        UUID subscriptionId = UUID.randomUUID();
        Subscription subscription = Subscription.builder()
                .url("http://example.com/rss")
                .keywords(List.of("test"))
                .build();
        ReflectionTestUtils.setField(subscription, "id", subscriptionId);
        Article article = new Article("Test Title", "Test Description", "http://example.com/article1");

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(rssParser.parse("http://example.com/rss")).thenReturn(List.of(article));
        lenient().when(notificationRepository.existsByLinkAndKeyword("http://example.com/article1", "test")).thenReturn(false);

        // when
        rssService.processRssSubscription(subscriptionId);

        // then
        verify(notificationRepository, times(1)).save(any());
    }

    @Test
    void processRssSubscription_shouldNotSaveNotification_whenKeywordIsNotFound() {
        // given
        UUID subscriptionId = UUID.randomUUID();
        Subscription subscription = Subscription.builder()
                .url("http://example.com/rss")
                .keywords(List.of("keyword"))
                .build();
        ReflectionTestUtils.setField(subscription, "id", subscriptionId);
        Article article = new Article("Test Title", "Test Description", "http://example.com/article1");

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(rssParser.parse("http://example.com/rss")).thenReturn(List.of(article));

        // when
        rssService.processRssSubscription(subscriptionId);

        // then
        verify(notificationRepository, never()).save(any());
    }
}
