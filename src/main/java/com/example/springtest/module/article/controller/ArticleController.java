package com.example.springtest.module.article.controller;

import com.example.springtest.module.article.repository.ArticleMongoRepository;
import com.example.springtest.module.article.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    ArticleMongoRepository articleMongoRepository;

    @GetMapping("/articles")
    public List<Article> getArticles(@RequestParam long from){
        return articleMongoRepository.findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(from);
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> postArticles(@RequestBody Article article) {
        articleMongoRepository.save(article);
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }
}
