package com.example.springtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Article extends Object {
    private String id;
    private String url;
    private String title;
    private long epochCreatedTime;
}
