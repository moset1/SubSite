package com.semocompany.subscriptionservice.scheduler;

import com.semocompany.subscriptionservice.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingTaskPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(CrawlingTask task) {
        log.info("Publishing crawling task for subscription id: {}", task.subscriptionId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "crawling.task.new", task);
    }
}