package com.semocompany.subscriptionservice.scheduler;

import com.semocompany.subscriptionservice.domain.SubscriptionType;
import java.io.Serializable;
import java.util.List;

public record CrawlingTask(
        Long subscriptionId,
        String url,
        SubscriptionType type,
        List<String> keywords
) implements Serializable {}