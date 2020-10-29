package com.example.springtest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheckController {
    @GetMapping("/health")
    public Map isRunning() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", "UP");

        return resultMap;
    }
}
