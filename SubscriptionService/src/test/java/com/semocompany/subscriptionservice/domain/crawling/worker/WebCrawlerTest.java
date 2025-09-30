package com.semocompany.subscriptionservice.domain.crawling.worker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WebCrawlerTest {
    private WebCrawler webCrawler;

    @BeforeEach
    void setUp() {
        webCrawler = new WebCrawler();
    }

    @Test
    @DisplayName("페이지네이션을 따라가며 모든 게시물 링크를 수집하는지 테스트")
    void crawlWithPagination() throws URISyntaxException {
        // given
        URL startUrl = getClass().getClassLoader().getResource("crawler/page1.html");
        assertThat(startUrl).isNotNull();

        String postLinkSelector = ".post-item a";
        String nextPageSelector = ".pagination a.next";

        // when
        List<Article> articles = webCrawler.crawl(startUrl.toURI().toString(), postLinkSelector, nextPageSelector);

        // then
        assertThat(articles).hasSize(3);
        assertThat(articles).extracting(Article::title)
                .containsExactly(
                        "게시물 1 (키워드: Java)",
                        "게시물 2 (키워드: Python)",
                        "게시물 3 (키워드: Docker)"
                );
    }
}