package com.semocompany.subscriptionservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(nullable = false, length = 512)
    private String title;

    @Column(nullable = false, length = 2048)
    private String link;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false, name = "discovered_at")
    private LocalDateTime discoveredAt;

    @Builder
    public Notification(Subscription subscription, String title, String link, String keyword) {
        this.subscription = subscription;
        this.title = title;
        this.link = link;
        this.keyword = keyword;
        this.discoveredAt = LocalDateTime.now();
    }
}