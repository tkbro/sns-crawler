package com.example.springtest.common.service;

import com.example.springtest.common.config.DiscordProperty;
import com.example.springtest.module.spotv.model.SpotvVideo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscordRelayApi {

    private final DiscordProperty discordProperty;
    private final RestTemplate restTemplate;

    public DiscordRelayApi(DiscordProperty discordProperty, RestTemplate restTemplate) {
        this.discordProperty = discordProperty;
        this.restTemplate = restTemplate;
    }

    public void postDiscordRelayServer(String channel, List<SpotvVideo> lists) {
        String PATH = "/discord";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        StringBuffer msg = new StringBuffer();
        for (SpotvVideo list : lists) {
            if (msg.length() != 0) {
                msg.append(", ");
            }
            msg.append(list.getTitle())
                .append(" - ")
                .append(list.getUrl());
        }

        map.put("channel", channel);
        map.put("msg", msg.toString());
        String body = null;
        try {
            body = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
        restTemplate.postForEntity(discordProperty.getEndpoint() + PATH, entity, String.class);
    }
}
