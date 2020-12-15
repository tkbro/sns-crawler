package com.example.springtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document (collection = "myCollection")
public class Article {
    private String id;
    private String url;
    private String title;
    private Long createdAt = Instant.now().toEpochMilli();
}
