package com.example.springtest.repository;

import com.example.springtest.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleMongoRepository extends MongoRepository<Article, String> {
}
