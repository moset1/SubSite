package com.semocompany.subscriptionservice.repository;

import com.semocompany.subscriptionservice.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("사용자 ID로 카테고리 목록 조회 통합 테스트")
    void findByUserId() {
        // given
        String userId1 = "user1";
        String userId2 = "user2";
        entityManager.persist(Category.builder().name("IT").userId(userId1).build());
        entityManager.persist(Category.builder().name("경제").userId(userId1).build());
        entityManager.persist(Category.builder().name("스포츠").userId(userId2).build());

        // when
        List<Category> categories = categoryRepository.findByUserId(userId1);

        // then
        assertThat(categories).hasSize(2);
        assertThat(categories).extracting(Category::getName).containsExactlyInAnyOrder("IT", "경제");
    }
}