package com.semocompany.subscriptionservice.domain.notification.dto;

import com.semocompany.subscriptionservice.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class NotificationDTO {

    private final UUID id;
    private final String title;
    private final String link;
    private final String keyword;
    private final LocalDateTime discoveredAt;
    private final UUID categoryId;
    private final String categoryName;

    @Builder
    public NotificationDTO(UUID id, String title, String link, String keyword, LocalDateTime discoveredAt, UUID categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.keyword = keyword;
        this.discoveredAt = discoveredAt;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public static NotificationDTO from(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .link(notification.getLink())
                .keyword(notification.getKeyword())
                .discoveredAt(notification.getDiscoveredAt())
                .categoryId(notification.getSubscription().getCategory().getId())
                .categoryName(notification.getSubscription().getCategory().getName())
                .build();
    }

    public static List<NotificationDTO> from(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationDTO::from)
                .collect(Collectors.toList());
    }

}
