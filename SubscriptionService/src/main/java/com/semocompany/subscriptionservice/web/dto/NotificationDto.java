package com.semocompany.subscriptionservice.web.dto;

import com.semocompany.subscriptionservice.domain.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotificationDto {

    private final Long id;
    private final String title;
    private final String link;
    private final String keyword;
    private final LocalDateTime discoveredAt;
    private final Long categoryId;
    private final String categoryName;

    @Builder
    public NotificationDto(Long id, String title, String link, String keyword, LocalDateTime discoveredAt, Long categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.keyword = keyword;
        this.discoveredAt = discoveredAt;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public static NotificationDto from(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .link(notification.getLink())
                .keyword(notification.getKeyword())
                .discoveredAt(notification.getDiscoveredAt())
                .categoryId(notification.getSubscription().getCategory().getId())
                .categoryName(notification.getSubscription().getCategory().getName())
                .build();
    }

    public static List<NotificationDto> from(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationDto::from)
                .collect(Collectors.toList());
    }
}