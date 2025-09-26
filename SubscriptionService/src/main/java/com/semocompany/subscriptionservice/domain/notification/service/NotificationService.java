package com.semocompany.subscriptionservice.domain.notification.service;


import com.semocompany.subscriptionservice.domain.notification.dto.NotificationDTO;
import com.semocompany.subscriptionservice.domain.notification.entity.Notification;
import com.semocompany.subscriptionservice.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationDTO> getNotifications(UUID userId, Optional<UUID> categoryId) {
        List<Notification> notifications;
        if (categoryId.isPresent()) {
            notifications = notificationRepository.findByCategoryIdAndUserId(categoryId.get(), userId);
        } else {
            notifications = notificationRepository.findByUserId(userId);
        }
        return NotificationDTO.from(notifications);
    }

}
