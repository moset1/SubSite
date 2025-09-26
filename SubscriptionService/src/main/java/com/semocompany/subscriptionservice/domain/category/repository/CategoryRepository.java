package com.semocompany.subscriptionservice.domain.category.repository;

import com.semocompany.subscriptionservice.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByUserId(UUID userId);
}
