package com.semocompany.subscriptionservice.service;

import com.semocompany.subscriptionservice.domain.Notification;
import com.semocompany.subscriptionservice.repository.NotificationRepository;
import com.semocompany.subscriptionservice.web.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getNotifications(String userId, Optional<Long> categoryId) {
        List<Notification> notifications;
        if (categoryId.isPresent()) {
            // This is a simplified authorization check. In a real app,
            // we should ensure the user owns this category.
            notifications = notificationRepository.findByCategoryId(categoryId.get());
        } else {
            notifications = notificationRepository.findByUserId(userId);
        }
        return NotificationDto.from(notifications);
    }
}