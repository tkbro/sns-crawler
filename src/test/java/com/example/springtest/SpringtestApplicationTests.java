package com.example.springtest;

import com.example.springtest.common.config.DiscordProperty;
import com.example.springtest.external.discord.DiscordRelayApi;
import com.example.springtest.module.spotv.repository.SpotvMongoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SpringtestApplicationTests {

    @Autowired
    DiscordProperty discordProperty;
    @Autowired
    DiscordRelayApi discordRelayApi;
    @Autowired
    SpotvMongoRepository spotvMongoRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void discordPropertyTest() {
        assertNotNull(discordProperty.getEndpoint());
    }

    @Test
    void postDiscord () {
        discordRelayApi.postDiscordRelayServer(discordProperty.getChannel(), spotvMongoRepository.findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(1611812399560L));
    }
}
