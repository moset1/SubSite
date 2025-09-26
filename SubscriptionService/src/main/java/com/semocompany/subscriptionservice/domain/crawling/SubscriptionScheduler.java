package com.semocompany.subscriptionservice.domain.crawling;

import com.semocompany.subscriptionservice.domain.crawling.task.CrawlingTask;
import com.semocompany.subscriptionservice.domain.crawling.task.CrawlingTaskPublisher;
import com.semocompany.subscriptionservice.domain.subscription.entity.Subscription;
import com.semocompany.subscriptionservice.domain.subscription.repository.SubscriptionRepository;
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

    @Scheduled(fixedRate = 6000000)
    public void scheduleSubscriptionChecks() {
        log.info("예약된 구독 확인 작업 시작");
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        if (subscriptions.isEmpty()) {
            log.info("확인한 구독이 존재하지 않습니다.");
            return;
        }

        log.info("{} 개의 구독을 확인합니다. Publishing 작업 중...", subscriptions.size());
        for (Subscription sub : subscriptions) {
            CrawlingTask task = new CrawlingTask(
                    sub.getId(),
                    sub.getUrl(),
                    sub.getType(),
                    sub.getKeywords()
            );
            publisher.publish(task);

        }
        log.info("Publishing 작업을 완료했습니다.");
    }
}
