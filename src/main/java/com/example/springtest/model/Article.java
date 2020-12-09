package com.example.springtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Article {
    private String id;
    private String url;
    private String title;
    private Long createdAt = Instant.now().toEpochMilli();
}
