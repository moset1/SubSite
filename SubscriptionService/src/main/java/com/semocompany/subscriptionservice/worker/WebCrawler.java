package com.semocompany.subscriptionservice.worker;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebCrawler {

    public List<Article> crawl(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            // This is a generic crawler. For a real-world scenario,
            // this would need to be much more sophisticated, likely
            // with site-specific CSS selectors.
            Elements links = doc.select("a[href]");

            return links.stream()
                    .map(link -> new Article(link.text(), link.absUrl("href")))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Error crawling website {}: {}", url, e.getMessage());
            return Collections.emptyList();
        }
    }
}