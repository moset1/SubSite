package com.semocompany.subscriptionservice.domain.crawling.service;

import com.semocompany.subscriptionservice.domain.crawling.worker.RssParser;
import com.semocompany.subscriptionservice.domain.notification.repository.NotificationRepository;
import com.semocompany.subscriptionservice.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final RssParser rssParser;
    private final WebCrawler webCrawler;

}
