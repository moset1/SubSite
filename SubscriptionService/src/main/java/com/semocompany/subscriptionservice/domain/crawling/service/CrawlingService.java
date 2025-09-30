package com.semocompany.subscriptionservice.domain.crawling.service;

import com.semocompany.subscriptionservice.domain.crawling.worker.Article;
import com.semocompany.subscriptionservice.domain.crawling.worker.RssParser;
import com.semocompany.subscriptionservice.domain.crawling.worker.WebCrawler;
import com.semocompany.subscriptionservice.domain.notification.entity.Notification;
import com.semocompany.subscriptionservice.domain.notification.repository.NotificationRepository;
import com.semocompany.subscriptionservice.domain.subscription.entity.Subscription;
import com.semocompany.subscriptionservice.domain.subscription.entity.SubscriptionType;
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
public class CrawlingService {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final RssParser rssParser;
    private final WebCrawler webCrawler;

    @Transactional
    public void processTask(UUID subscriptionId, String url, List<String> keywords) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElse(null);

        if (subscription == null) {
            log.warn("해당 id의 구독을 찾을 수 없습니다 : {}", subscriptionId);
            return;
        }

        log.info("해당 URL의 구독 프로세스 시작 : {}", url);
        List<Article> articles;

        switch (subscription.getType()) {
            case RSS:
                articles = rssParser.parse(url);
                break;
            case CRAWL:
                if (subscription.getPostLinkSelector() != null && subscription.getNextPageSelector() != null) {
                    articles = webCrawler.crawl(url, subscription.getPostLinkSelector(), subscription.getNextPageSelector());
                } else {
                    log.warn("해당 구독에 CSS 선택자가 존재하지 않습니다. 스킵합니다. : {}", subscription.getId());
                    articles = List.of();
                }
                break;
            default:
                articles = List.of();
        }

        for (Article article : articles) {
            for (String keyword : keywords) {
                boolean isKeywordFound = article.title().toLowerCase().contains(keyword.toLowerCase());

                if (!isKeywordFound && subscription.getType() == SubscriptionType.RSS) {
                    isKeywordFound = article.description().toLowerCase().contains(keyword.toLowerCase());
                }

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
