package com.semocompany.subscriptionservice.domain.crawling.worker;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebCrawler {

    public List<Article> crawl(String url, String postLinkSelector, String nextPageSelector) {
        List<Article> articles = new ArrayList<>();
        String currentUrl = url;

        while (currentUrl != null && !currentUrl.isEmpty()) {
            try {
                Document doc = getDocument(currentUrl);
                if (doc == null) {
                    break;
                }

                Elements articleElements = doc.select(postLinkSelector);
                articles.addAll(articleElements.stream()
                        .map(element -> new Article(element.text(), element.absUrl("href"), ""))
                        .collect(Collectors.toList()));

                Element nextPageLink = doc.selectFirst(nextPageSelector);
                if (nextPageLink != null) {
                    currentUrl = nextPageLink.absUrl("href");
                } else {
                    currentUrl = null;
                }

            } catch (IOException | URISyntaxException e) {
                log.error("웹사이트 크롤링 중 에러 발생 {}: {}", currentUrl, e.getMessage());
                break;
            }
        }
        return articles;
    }

    private Document getDocument(String url) throws IOException, URISyntaxException {
        if (url.startsWith("file")) {
            return Jsoup.parse(new File(new URI(url)), "UTF-8", url);
        } else {
            return Jsoup.connect(url).get();
        }
    }
}
