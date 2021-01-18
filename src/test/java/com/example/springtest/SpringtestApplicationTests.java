package com.example.springtest;

import com.example.springtest.common.config.DiscordProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SpringtestApplicationTests {

    @Autowired
    DiscordProperty discordProperty;

    @Test
    void contextLoads() {
    }

    @Test
    void discordPropertyTest() {
        assertNotNull(discordProperty.getAddress());
    }
}
