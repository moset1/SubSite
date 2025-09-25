package com.semocompany.subscriptionservice.web.controller;

import com.semocompany.subscriptionservice.service.NotificationService;
import com.semocompany.subscriptionservice.web.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(
            @RequestHeader("X-USER-ID") String userId,
            @RequestParam("categoryId") Optional<Long> categoryId) {

        List<NotificationDto> notifications = notificationService.getNotifications(userId, categoryId);
        return ResponseEntity.ok(notifications);
    }
}