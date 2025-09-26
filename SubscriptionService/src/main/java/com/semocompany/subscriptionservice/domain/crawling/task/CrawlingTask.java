package com.semocompany.subscriptionservice.domain.crawling.task;

import com.semocompany.subscriptionservice.domain.subscription.entity.SubscriptionType;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record CrawlingTask(
        UUID subscriptionId,
        String url,
        SubscriptionType type,
        List<String> keywords
) implements Serializable {}  // 추후에 gRPC 도입하기
