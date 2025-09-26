package com.semocompany.subscriptionservice.domain.subscription.service;

import com.semocompany.subscriptionservice.domain.category.entity.Category;
import com.semocompany.subscriptionservice.domain.category.repository.CategoryRepository;
import com.semocompany.subscriptionservice.domain.subscription.dto.SubscriptionDTO;
import com.semocompany.subscriptionservice.domain.subscription.entity.Subscription;
import com.semocompany.subscriptionservice.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final CrawlingTaskPublisher publisher;

    @Transactional
    public SubscriptionDTO.Response createSubscription(SubscriptionDTO.CreateRequest request, UUID userId) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 카테고리가 존재하지 않습니다." + request.getCategoryId()));

        if (!category.getUserId().equals(userId)) {
            throw new SecurityException("사용자에게 해당 카테고리에 대한 권한이 존재하지 않습니다.");
        }

        Subscription subscription = Subscription.builder()
                .category(category)
                .url(request.getUrl())
                .keywords(request.getKeywords())
                .type(request.getType())
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return SubscriptionDTO.Response.from(savedSubscription);
    }

    @Transactional
    public SubscriptionDTO.Response updateSubscription(UUID subscriptionId, SubscriptionDTO.UpdateRequest request, UUID userId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 Subscription을 찾을 수 없습니다. : " + subscriptionId));

        if(!subscription.getCategory().getUserId().equals(userId)) {
            throw new SecurityException("이 구독에 대한 권한이 없습니다.");
        }

        subscription.updateKeywords(request.getKeywords());
        return SubscriptionDTO.Response.from(subscription);
    }

    @Transactional
    public void deleteSubscription(UUID subscriptionId, UUID userId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 Subscription을 찾을 수 없습니다. : " + subscriptionId));

        if(!subscription.getCategory().getUserId().equals(userId)) {
            throw new SecurityException("이 구독에 대한 권한이 없습니다.");
        }

        subscriptionRepository.delete(subscription);
    }

    public List<SubscriptionDTO.Response> getUserSubscriptions(UUID userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByCategory_UserId(userId);
        return SubscriptionDTO.Response.from(subscriptions);
    }

    public void checkUpdateForUser(UUID userId) {
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
