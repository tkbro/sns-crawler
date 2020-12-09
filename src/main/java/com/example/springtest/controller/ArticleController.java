package com.example.springtest.controller;

import com.example.springtest.model.Article;
import com.example.springtest.repository.ArticleDataStore;
import com.example.springtest.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/items")
    public Map<Long, Object> getArgs(@RequestParam long from){
        // TODO: Validate param
        Map<Long, Object> resultMap = new HashMap<>();
        for(Article article : articleRepository.findBeforeCreatedAt(from)) {
            resultMap.put(article.getEpochCreatedTime(), article);
        }
        return resultMap;
    }

    @PostMapping("/items")
    public Map<String, Object> postArgs(@RequestBody Article article) {
        Map<String, Object> resultMap = new HashMap<>();
        articleRepository.save(article);
        resultMap.put("article", article);
        return resultMap;
    }
}
