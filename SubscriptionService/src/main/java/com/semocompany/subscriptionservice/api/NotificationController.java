package com.semocompany.subscriptionservice.api;

import com.semocompany.subscriptionservice.domain.notification.dto.NotificationDTO;
import com.semocompany.subscriptionservice.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestParam("categoryId") Optional<UUID> categoryId) {

        List<NotificationDTO> notifications = notificationService.getNotifications(userId, categoryId);
        return ResponseEntity.ok(notifications);
    }
}
