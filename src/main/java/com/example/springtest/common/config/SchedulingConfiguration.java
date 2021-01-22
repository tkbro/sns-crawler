package com.example.springtest.common.config;

import com.example.springtest.module.spotv.model.SpotvVideo;
import com.example.springtest.module.spotv.service.SpotvSchedulingService;
import com.example.springtest.module.jirye.model.JiryeRoom;
import com.example.springtest.module.jirye.service.JiryeSchedulingService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
public class SchedulingConfiguration {

    private final JiryeSchedulingService jiryeSchedulingService;
    private final SpotvSchedulingService spotvSchedulingService;

    public SchedulingConfiguration(JiryeSchedulingService jiryeSchedulingService,
                                   SpotvSchedulingService spotvSchedulingService) {
        this.jiryeSchedulingService = jiryeSchedulingService;
        this.spotvSchedulingService = spotvSchedulingService;
    }

    @Scheduled(initialDelay = 100, fixedRate = 3600000)  // TODO: Extract param as properties
    public void executeJirye() {
        final List<JiryeRoom> crawledResult = jiryeSchedulingService.crawl();
        jiryeSchedulingService.handleCrawledResult(crawledResult);
    }

    @Scheduled(initialDelay = 1000, fixedRate = 600000)  // TODO: Extract param as properties
    public void executeSpotv() {
        final List<SpotvVideo> crawledResult = spotvSchedulingService.crawl();
        spotvSchedulingService.handleCrawledResult(crawledResult);
    }
}
