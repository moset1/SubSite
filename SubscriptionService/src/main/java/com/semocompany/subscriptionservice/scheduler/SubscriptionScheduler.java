package com.semocompany.subscriptionservice.scheduler;

import com.semocompany.subscriptionservice.domain.Subscription;
import com.semocompany.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final CrawlingTaskPublisher publisher;

    // Run every 10 minutes
    @Scheduled(fixedRate = 600000)
    public void scheduleSubscriptionChecks() {
        log.info("Starting scheduled subscription check...");
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        if (subscriptions.isEmpty()) {
            log.info("No subscriptions to check.");
            return;
        }

        log.info("Found {} subscriptions to check. Publishing tasks...", subscriptions.size());
        for (Subscription sub : subscriptions) {
            CrawlingTask task = new CrawlingTask(
                    sub.getId(),
                    sub.getUrl(),
                    sub.getType(),
                    sub.getKeywords()
            );
            publisher.publish(task);
        }
        log.info("Finished publishing tasks for all subscriptions.");
    }
}