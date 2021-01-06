package com.example.springtest.module.spotv.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "spotvVideo")
public class SpotvVideo {

    @Indexed(unique = true)
    private String videoId;

    private String url;

    private String title;

    @Indexed(direction = IndexDirection.DESCENDING)
    private Long createdAt = Instant.now().toEpochMilli();
}
