package com.example.springtest.controller;


import com.example.springtest.Article;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PostController {
    @PostMapping("/items")
    public Map postArgs(@RequestBody Article article){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("article", article);
        return resultMap;
    }
}
