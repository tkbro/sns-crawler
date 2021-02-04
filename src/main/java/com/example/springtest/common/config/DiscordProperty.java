package com.example.springtest.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "discord")
public class DiscordProperty {
    private String endpoint;
    private String channel;
    private List<String> keywords;
}
