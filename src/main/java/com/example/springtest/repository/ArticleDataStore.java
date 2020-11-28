package com.example.springtest.repository;

import com.example.springtest.model.Article;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
public class ArticleDataStore extends ConcurrentSkipListMap<Long, Article> {

    // TODO: Create a method that returns List<Article> using subMap(from, to)

    public List<Article> subList(Long fromKey, Long toKey) {
        return new ArrayList<Article>(super.subMap(fromKey, toKey).values());
    }
}
