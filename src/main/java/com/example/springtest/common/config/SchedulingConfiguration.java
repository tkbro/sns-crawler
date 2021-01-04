package com.example.springtest.common.config;

import com.example.springtest.module.jirye.model.JiryeRoom;
import com.example.springtest.module.jirye.service.JiryeSchedulingService;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class SchedulingConfiguration {

    private final JiryeSchedulingService jiryeSchedulingService;

    public SchedulingConfiguration(JiryeSchedulingService jiryeSchedulingService) {
        this.jiryeSchedulingService = jiryeSchedulingService;
    }

    @Scheduled(initialDelay = 100, fixedRate = 3600000)  // TODO: Extract param as properties
    public void executeJirye() {
        final List<JiryeRoom> crawledResult = jiryeSchedulingService.crawl();
        jiryeSchedulingService.handleCrawledResult(crawledResult);
    }
}
