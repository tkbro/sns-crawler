package com.example.springtest;

import com.example.springtest.model.Article;
import com.example.springtest.repository.ArticleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.springtest.repository.ArticleDataStore;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringtestApplicationTests {

    @Autowired
    ArticleDataStore articleDataStore;

    @Autowired
    ArticleRepository articleRepository;

    @Test
	void contextLoads() {
	}

	@Test
    void articleDataStoreTest() {
        articleDataStore.put(1L, new Article("1", "url", "title", 1L));
        articleDataStore.put(3L, new Article("1", "url", "title", 3L));
        articleDataStore.put(2L, new Article("1", "url", "title", 2L));
        articleDataStore.put(4L, new Article("1", "url", "title", 4L));

        List<Article> articles = articleDataStore.subList(2L, 5L);
        System.out.println(articles);

        assertEquals(3, articles.size());
        assertEquals(2L, articles.get(0).getEpochCreatedTime());
        assertEquals(3L, articles.get(1).getEpochCreatedTime());
        assertEquals(4L, articles.get(2).getEpochCreatedTime());
    }

    @Test
    void articleDataStoreTest2() {
        articleDataStore.put(1L, new Article("1", "url", "title", 1L));
        articleDataStore.put(3L, new Article("1", "url", "title", 3L));
        articleDataStore.put(2L, new Article("1", "url", "title", 2L));
        articleDataStore.put(5L, new Article("1", "url", "title", 5L));

        List<Article> articles = articleDataStore.subList(2L, 6L);
        System.out.println(articles);

        assertEquals(3, articles.size());
        assertEquals(2L, articles.get(0).getEpochCreatedTime());
        assertEquals(3L, articles.get(1).getEpochCreatedTime());
        assertEquals(5L, articles.get(2).getEpochCreatedTime());
    }

    @Test
    void articleRepositoryTest() {
        articleRepository.save(new Article("1", "url", "title", 5L));
        articleRepository.save(new Article("1", "url", "title", 5L));
        articleRepository.save(new Article("1", "url", "title", 5L));
        Map<String, Object> resultMap = new HashMap<>();
        for(Article article : articleRepository.findBeforeCreatedAt(0)) {
            resultMap.put("article", article);
        }
        System.out.println(resultMap);
    }
}
