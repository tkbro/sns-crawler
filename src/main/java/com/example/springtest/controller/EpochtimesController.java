package com.example.springtest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EpochtimesController {
    @GetMapping("/items")
    public Map getArgs(@RequestParam long from){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("epochtimes", from);
            return resultMap;
    }
}
