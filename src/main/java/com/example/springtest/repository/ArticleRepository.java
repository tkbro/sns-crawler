package com.example.springtest.repository;

import com.example.springtest.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Instant;
import java.util.List;

@Repository
public class ArticleRepository {

    private ArticleDataStore articleDataStore;

    @Autowired
    public ArticleRepository(ArticleDataStore articleDataStore) {
        this.articleDataStore = articleDataStore;
    }

    public List<Article> findBeforeCreatedAt(long createAt) {
        return articleDataStore.subList(createAt, Instant.now().toEpochMilli());
    }

    public void save(Article article) {
        articleDataStore.put(article.getEpochCreatedTime(), article);
    }
}
