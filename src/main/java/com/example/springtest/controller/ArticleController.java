package com.example.springtest.controller;

import com.example.springtest.model.Article;
import com.example.springtest.repository.ArticleMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController {

    @Autowired
    ArticleMongoRepository articleMongoRepository;

    @GetMapping("/articles")
    public List<Article> getArticles(@RequestParam long from){
        return articleMongoRepository.findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(from);
    }

    @PostMapping("/articles")
    public Map<String, Object> postArticles(@RequestBody Article article) {
        Map<String, Object> resultMap = new HashMap<>();
        articleMongoRepository.save(article);
        resultMap.put("article", article);
        return resultMap;
    }
}
