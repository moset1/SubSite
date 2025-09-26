package com.semocompany.subscriptionservice.domain.subscription.repository;

import com.semocompany.subscriptionservice.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByCategoryId(UUID categoryId);
    List<Subscription> findByCategory_UserId(UUID userId);
}
