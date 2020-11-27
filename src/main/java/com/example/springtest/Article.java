package com.example.springtest;

public class Article {
    private String id;
    private String url;
    private String title;
    private long epochCreatedTime;

    public Article(String id, String url, String title, long epochCreatedTime){
        this.id = id;
        this.url = url;
        this.title = title;
        this.epochCreatedTime = epochCreatedTime;
    }

    public String getId() {
        return id;
    }

    public String getURL() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public long getEpochCreatedTime() {
        return epochCreatedTime;
    }
}
