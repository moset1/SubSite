package com.semocompany.subscriptionservice.domain.crawling.worker;

import com.semocompany.subscriptionservice.config.RabbitMQConfig;
import com.semocompany.subscriptionservice.domain.crawling.service.CrawlingService;
import com.semocompany.subscriptionservice.domain.crawling.task.CrawlingTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingTaskConsumer {

    private final CrawlingService crawlingService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(CrawlingTask task) {
        log.info("Received crawling task for subscription id: {}", task.subscriptionId());
        try {
            crawlingService.processTask(task.subscriptionId(), task.url(), task.keywords());
        } catch (Exception e) {
            log.error("Error processing task for subscription id: {}", task.subscriptionId(), e);
            // Here you might want to implement a dead-letter queue or other error handling mechanism
        }
    }
}
