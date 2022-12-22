package codes.dirty.sns.crawler.module.race.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RaceConditionSearchResultSchedulingServiceTest {

    @Test
    void searchTest() {
        RaceResultCrawlingService raceResultCrawlingService = new RaceResultCrawlingService();
        RaceConditionSearchResultCrawlingService raceConditionSearchResultCrawlingService = new RaceConditionSearchResultCrawlingService(raceResultCrawlingService);
        RaceConditionSearchResultSchedulingService raceConditionSearchResultSchedulingService = new RaceConditionSearchResultSchedulingService(raceConditionSearchResultCrawlingService);
        raceConditionSearchResultSchedulingService.getSearchResultByYearMonth(2022, 9);
    }
}
