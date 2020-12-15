package com.example.springtest.repository;

import com.example.springtest.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleMongoRepository extends MongoRepository<Article, String> {
    List<Article> findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(Long createdAt);
}
