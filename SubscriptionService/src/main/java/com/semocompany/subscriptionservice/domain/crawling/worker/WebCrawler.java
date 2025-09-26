package com.semocompany.subscriptionservice.domain.crawling.worker;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebCrawler {

    public List<Article> crawl(String url, String articleListSelector, String titleLinkSelector) {
        try {
            Document doc = Jsoup.connect(url).get();

            Elements articleElements = doc.select(articleListSelector);

            return articleElements.stream()
                    .map(element -> {
                        Element link = element.selectFirst(titleLinkSelector);
                        if (link != null) {
                            return new Article(link.text(), link.absUrl("href"));
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("웹사이트 크롤링 중 에러 발생 {}: {}", url, e.getMessage());
            return Collections.emptyList();
        }
    }
}
