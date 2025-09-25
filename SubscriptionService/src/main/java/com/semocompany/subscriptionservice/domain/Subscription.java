package com.semocompany.subscriptionservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String url;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "subscription_keywords", joinColumns = @JoinColumn(name = "subscription_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType type;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    @Builder
    public Subscription(Category category, String url, List<String> keywords, SubscriptionType type) {
        this.category = category;
        this.url = url;
        this.keywords = keywords;
        this.type = type;
        this.lastCheckedAt = LocalDateTime.now();
    }

    public void updateKeywords(List<String> newKeywords) {
        this.keywords = newKeywords;
    }

    public void updateLastCheckedTime() {
        this.lastCheckedAt = LocalDateTime.now();
    }
}