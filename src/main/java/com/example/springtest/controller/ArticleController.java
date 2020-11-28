package com.example.springtest.controller;

import com.example.springtest.model.Article;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ArticleController {
    @GetMapping("/items")
    public Map<String, Object> getArgs(@RequestParam long from){
        // TODO: Validate param
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("epochtimes", from);
        return resultMap;
    }

    @PostMapping("/items")
    public Map<String, Object> postArgs(@RequestBody Article article) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("article", article);
        return resultMap;
    }
}
