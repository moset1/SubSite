package com.semocompany.subscriptionservice.repository;

import com.semocompany.subscriptionservice.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(String userId);
}