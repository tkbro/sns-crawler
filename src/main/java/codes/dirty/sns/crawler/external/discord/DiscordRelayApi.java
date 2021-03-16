package codes.dirty.sns.crawler.external.discord;

import codes.dirty.sns.crawler.common.config.DiscordProperty;
import codes.dirty.sns.crawler.common.util.ObjectMapperUtils;
import codes.dirty.sns.crawler.module.spotv.model.SpotvVideo;
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

    public void postDiscordRelayServer(String channel, String msg) {

        String body = ObjectMapperUtils.toJsonByObject(buildRequest(channel, msg));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
        restTemplate.postForEntity(discordProperty.getEndpoint() + PATH, entity, String.class);
    }

    private DiscordRequest buildRequest(String channel, String msg) {
        return DiscordRequest.builder()
                             .channel(channel)
                             .msg(msg)
                             .build();
    }
}
