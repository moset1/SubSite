package com.semocompany.subscriptionservice.service;

import com.semocompany.subscriptionservice.domain.Category;
import com.semocompany.subscriptionservice.domain.Subscription;
import com.semocompany.subscriptionservice.repository.CategoryRepository;
import com.semocompany.subscriptionservice.repository.SubscriptionRepository;
import com.semocompany.subscriptionservice.scheduler.CrawlingTask;
import com.semocompany.subscriptionservice.scheduler.CrawlingTaskPublisher;
import com.semocompany.subscriptionservice.web.dto.SubscriptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final CrawlingTaskPublisher publisher;

    @Transactional
    public SubscriptionDto.Response createSubscription(SubscriptionDto.CreateRequest request, String userId) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + request.getCategoryId()));

        if (!category.getUserId().equals(userId)) {
            throw new SecurityException("User does not have permission to use this category");
        }

        Subscription subscription = Subscription.builder()
                .category(category)
                .url(request.getUrl())
                .keywords(request.getKeywords())
                .type(request.getType())
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return SubscriptionDto.Response.from(savedSubscription);
    }

    @Transactional
    public SubscriptionDto.Response updateSubscription(Long subscriptionId, SubscriptionDto.UpdateRequest request, String userId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + subscriptionId));

        if (!subscription.getCategory().getUserId().equals(userId)) {
            throw new SecurityException("User does not have permission to update this subscription");
        }

        subscription.updateKeywords(request.getKeywords());
        return SubscriptionDto.Response.from(subscription);
    }

    @Transactional
    public void deleteSubscription(Long subscriptionId, String userId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + subscriptionId));

        if (!subscription.getCategory().getUserId().equals(userId)) {
            throw new SecurityException("User does not have permission to delete this subscription");
        }

        subscriptionRepository.delete(subscription);
    }

    public List<SubscriptionDto.Response> getUserSubscriptions(String userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByCategory_UserId(userId);
        return SubscriptionDto.Response.from(subscriptions);
    }

    public void checkUpdatesForUser(String userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByCategory_UserId(userId);
        for (Subscription sub : subscriptions) {
            CrawlingTask task = new CrawlingTask(
                    sub.getId(),
                    sub.getUrl(),
                    sub.getType(),
                    sub.getKeywords()
            );
            publisher.publish(task);
        }
    }
}