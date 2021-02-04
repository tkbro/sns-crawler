package com.example.springtest.external.discord;

import com.example.springtest.common.config.DiscordProperty;
import com.example.springtest.common.util.ObjectMapperUtils;
import com.example.springtest.module.spotv.model.SpotvVideo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class DiscordRelayApi {

    private final DiscordProperty discordProperty;
    private final RestTemplate restTemplate;
    private static final String PATH = "/discord";

    public DiscordRelayApi(DiscordProperty discordProperty, RestTemplate restTemplate) {
        this.discordProperty = discordProperty;
        this.restTemplate = restTemplate;
    }

    public void postDiscordRelayServer(List<SpotvVideo> spotvVideos, String channel) {

        String body = ObjectMapperUtils.toJsonByObject(buildRequest(channel, spotvVideos));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
        restTemplate.postForEntity(discordProperty.getEndpoint() + PATH, entity, String.class);
    }

    private DiscordRequest buildRequest(String channel, List<SpotvVideo> spotvVideos) {
        return DiscordRequest.builder()
                             .channel(channel)
                             .msg(formatRequestMsg(spotvVideos).toString())
                             .build();
    }

    private StringBuilder formatRequestMsg(List<SpotvVideo> spotvVideos) {
        StringBuilder msg = new StringBuilder();
        for (SpotvVideo video : spotvVideos) {
            if (msg.length() != 0) {
                msg.append(", ");
            }
            msg.append(video.getTitle())
               .append(" - ")
               .append(video.getUrl());
        }
        return msg;
    }
}
