package com.semocompany.subscriptionservice.domain.crawling.service;

import com.semocompany.subscriptionservice.domain.crawling.worker.Article;
import com.semocompany.subscriptionservice.domain.crawling.worker.RssParser;
import com.semocompany.subscriptionservice.domain.notification.entity.Notification;
import com.semocompany.subscriptionservice.domain.notification.repository.NotificationRepository;
import com.semocompany.subscriptionservice.domain.subscription.entity.Subscription;
import com.semocompany.subscriptionservice.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssService {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final RssParser rssParser;

    @Transactional
    public void processRssSubscription(UUID subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElse(null);

        if (subscription == null) {
            log.warn("해당 id의 구독을 찾을 수 없습니다 : {}", subscriptionId);
            return;
        }

        log.info("해당 URL의 구독 프로세스 시작 : {}", subscription.getUrl());
        List<Article> articles = rssParser.parse(subscription.getUrl());

        for (Article article : articles) {
            for (String keyword : subscription.getKeywords()) {
                boolean isKeywordFound = article.title().toLowerCase().contains(keyword.toLowerCase()) ||
                                         article.description().toLowerCase().contains(keyword.toLowerCase());

                if (isKeywordFound) {
                    if (!notificationRepository.existsByLinkAndKeyword(article.link(), keyword)) {
                        Notification notification = Notification.builder()
                                .subscription(subscription)
                                .title(article.title())
                                .link(article.link())
                                .keyword(keyword)
                                .build();
                        notificationRepository.save(notification);
                        log.info("새로운 알림 저장: '{}' for 키워드 '{}'", article.title(), keyword);
                    }
                }
            }
        }
        subscription.updateLastCheckedTime();
    }
}
