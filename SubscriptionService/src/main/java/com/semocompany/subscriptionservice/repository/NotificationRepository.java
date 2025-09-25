package com.semocompany.subscriptionservice.repository;

import com.semocompany.subscriptionservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByLinkAndKeyword(String link, String keyword);

    @Query("SELECT n FROM Notification n JOIN n.subscription s WHERE s.category.userId = :userId ORDER BY n.discoveredAt DESC")
    List<Notification> findByUserId(@Param("userId") String userId);

    @Query("SELECT n FROM Notification n JOIN n.subscription s WHERE s.category.id = :categoryId ORDER BY n.discoveredAt DESC")
    List<Notification> findByCategoryId(@Param("categoryId") Long categoryId);
}