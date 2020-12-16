package com.example.springtest;

import com.example.springtest.model.Article;
import com.example.springtest.repository.ArticleMongoRepository;
import com.example.springtest.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.springtest.repository.ArticleDataStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringtestApplicationTests {

    @Autowired
    ArticleMongoRepository articleMongoRepository;

    @Test
	void contextLoads() {
	}

    // TODO: remove this
    @Test
    void articleMongoRepositoryTest() {
        articleMongoRepository.save(new Article("1", "url", "title", 5L));
    }
}
