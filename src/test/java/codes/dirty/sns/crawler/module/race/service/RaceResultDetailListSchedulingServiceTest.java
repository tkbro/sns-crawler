package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.util.ObjectMapperUtils;
import codes.dirty.sns.crawler.module.race.model.RaceData;
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
class RaceResultDetailListSchedulingServiceTest {
    private final String PROFILE_LIST_SAMPLE = "RACE_result_list_sample.html";
    @Spy
    private RaceResultCrawlingService raceResultCrawlingService;
    @Spy
    private RaceProfileCrawlingService raceProfileCrawlingService;

    @Spy
    @InjectMocks
    private RaceResultListSchedulingService raceResultListSchedulingService;

    @Test
    void parseTest() throws IOException {
        // given
        ClassLoader classLoader = getClass().getClassLoader();
        final Document doc = Jsoup.parse(new File(classLoader.getResource("html").getPath() + "\\" + PROFILE_LIST_SAMPLE), null);
        List<RaceData> result = new ArrayList<>();

        // when
        result = raceResultListSchedulingService.parseHtmlToRaceDataList(doc);

        // then
        String raceDataListString = ObjectMapperUtils.toJsonByObject(result);
        log.info("result : \n{}", raceDataListString);
    }
}
