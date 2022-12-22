package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.util.ObjectMapperUtils;
import codes.dirty.sns.crawler.module.race.model.RaceConditionSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RaceConditionSearchResultCrawlingServiceTest {
    private final String SAMPLE = "RACE_condition_search_list_sample.html";
    @Spy
    @InjectMocks
    private RaceConditionSearchResultCrawlingService raceConditionSearchResultCrawlingService;

    @Test
    void parseTest() throws IOException {
        // given
        ClassLoader classLoader = getClass().getClassLoader();
        final Document doc = Jsoup.parse(new File(classLoader.getResource("html").getPath() + "\\" + SAMPLE), null);
        List<RaceConditionSearchResult> result = new ArrayList<>();

        // when
        result = raceConditionSearchResultCrawlingService.parseConditionSearchResultDocument(doc);

        // then
        String raceDataListString = ObjectMapperUtils.toJsonByObject(result);
        log.info("result : \n{}", raceDataListString);
    }
}
