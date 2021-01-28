package com.example.springtest.external.discord;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscordRequest {

    private String channel;
    private String msg;
}
