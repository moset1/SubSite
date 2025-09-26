package com.semocompany.subscriptionservice.domain.crawling.worker;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RssParser {

    public List<Article> parse(String urlString) {

        try {
            URL url = new URL(urlString);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            return feed.getEntries().stream()
                    .map(this::toArticle)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("RSS 피드 파싱 오류 {}: {}", urlString, e.getMessage());
            return Collections.emptyList();
        }


    }

    private Article toArticle(SyndEntry entry) {
        return new Article(entry.getTitle(), entry.getLink());
    }
}
