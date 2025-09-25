package com.semocompany.subscriptionservice.repository;

import com.semocompany.subscriptionservice.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByCategoryId(Long categoryId);
    List<Subscription> findByCategory_UserId(String userId);
}