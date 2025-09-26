package com.semocompany.subscriptionservice.domain.subscription.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.semocompany.subscriptionservice.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String url;

    @ElementCollection(fetch = FetchType.LAZY)
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
