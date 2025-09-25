package com.semocompany.subscriptionservice.service;

import com.semocompany.subscriptionservice.domain.Notification;
import com.semocompany.subscriptionservice.domain.Subscription;
import com.semocompany.subscriptionservice.repository.NotificationRepository;
import com.semocompany.subscriptionservice.repository.SubscriptionRepository;
import com.semocompany.subscriptionservice.worker.Article;
import com.semocompany.subscriptionservice.worker.RssParser;
import com.semocompany.subscriptionservice.worker.WebCrawler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final RssParser rssParser;
    private final WebCrawler webCrawler;

    @Transactional
    public void processTask(Long subscriptionId, String url, List<String> keywords) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElse(null);
        if (subscription == null) {
            log.warn("Subscription not found for id: {}, skipping task.", subscriptionId);
            return;
        }

        log.info("Processing subscription for URL: {}", url);
        List<Article> articles = switch (subscription.getType()) {
            case RSS -> rssParser.parse(url);
            case CRAWL -> webCrawler.crawl(url);
        };

        for (Article article : articles) {
            for (String keyword : keywords) {
                if (article.title().toLowerCase().contains(keyword.toLowerCase())) {
                    if (!notificationRepository.existsByLinkAndKeyword(article.link(), keyword)) {
                        Notification notification = Notification.builder()
                                .subscription(subscription)
                                .title(article.title())
                                .link(article.link())
                                .keyword(keyword)
                                .build();
                        notificationRepository.save(notification);
                        log.info("New notification saved: '{}' for keyword '{}'", article.title(), keyword);
                    }
                }
            }
        }
        subscription.updateLastCheckedTime();
    }
}