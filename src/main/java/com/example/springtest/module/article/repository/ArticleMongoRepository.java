package com.example.springtest.module.article.repository;

import com.example.springtest.module.article.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleMongoRepository extends MongoRepository<Article, String> {
    List<Article> findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(Long createdAt);
}
