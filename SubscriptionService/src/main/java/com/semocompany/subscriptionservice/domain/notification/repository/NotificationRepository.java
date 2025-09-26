package com.semocompany.subscriptionservice.domain.notification.repository;

import com.semocompany.subscriptionservice.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    boolean existsByLinkAndKeyword(String link, String keyword);

    @Query("SELECT n FROM Notification n JOIN n.subscription s WHERE s.category.userId = :userId ORDER BY n.discoveredAt DESC")
    List<Notification> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT n FROM Notification n JOIN n.subscription s WHERE s.category.id = :categoryId AND s.category.userId = :userId ORDER BY n.discoveredAt DESC")
    List<Notification> findByCategoryIdAndUserId(@Param("categoryId") UUID categoryId, @Param("userId") UUID userId);
}
