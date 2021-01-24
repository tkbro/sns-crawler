package com.example.springtest.common.service;

import com.example.springtest.common.config.DiscordProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DiscordRelayApi {

    private final DiscordProperty discordProperty;
    private final RestTemplate restTemplate;

    public DiscordRelayApi(DiscordProperty discordProperty, RestTemplate restTemplate) {
        this.discordProperty = discordProperty;
        this.restTemplate = restTemplate;
    }

    public void postDiscordRelayServer() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"channel\" : \"일반\", \"msg\" : \"축구3\"}";
        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
        restTemplate.postForEntity(discordProperty.getAddress(), entity, String.class);
    }
}
