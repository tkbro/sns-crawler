package codes.dirty.sns.crawler.common.config;

import codes.dirty.sns.crawler.module.spotv.service.SpotvSchedulingService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class NoScheduleTestConfiguration {
    @MockBean
    SchedulingConfiguration schedulingConfiguration;
    @MockBean
    SpotvSchedulingService spotvSchedulingService;
}
