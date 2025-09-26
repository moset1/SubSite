package com.semocompany.subscriptionservice.domain.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "categories")
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, name = "user_id")
    private UUID userId;  // User의 UUID

    @Builder
    public Category(String name, UUID userId) {
        this.name = name;
        this.userId = userId;
    }


}
