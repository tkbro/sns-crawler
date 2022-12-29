package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.module.race.model.RaceResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RaceResultDetailCrawlingServiceTest {
    private final String RESULT_SAMPLE = "RACE_result_sample.html";
    @Spy
    private RaceResultCrawlingService raceResultCrawlingService;

    @Test
    void parseTest() throws IOException {
        // given
        ClassLoader classLoader = getClass().getClassLoader();
        final Document doc = Jsoup.parse(new File(classLoader.getResource("html").getPath() + "\\" + RESULT_SAMPLE), null);

        // when
        List<RaceResult> parseList = raceResultCrawlingService.parseHtmlForRaceResult(doc);
        RaceResult result = parseList.stream()
            .sorted(Comparator.comparing(o -> o.getRaceResultDetail().getRank()))
            .findFirst().orElseThrow(() -> new RuntimeException());

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getRaceResultDetail().getRank(), "1");
        Assertions.assertEquals(result.getRaceRecordDetail().getRank(), "1");

        Assertions.assertEquals(result.getRaceResultDetail().getBreeder(), "이신영");
        Assertions.assertEquals(result.getRaceResultDetail().getWinRate(), "2.7");

        Assertions.assertEquals(result.getRaceRecordDetail().getS1f(), "0:13.6");
        Assertions.assertEquals(result.getRaceRecordDetail().getThirdCorner(), "0:13.6");
        Assertions.assertEquals(result.getRaceRecordDetail().getG3f(), "0:24.3");
        Assertions.assertEquals(result.getRaceRecordDetail().getFourthCorner(), "0:30.3");
        Assertions.assertEquals(result.getRaceRecordDetail().getG1f(), "0:48.5");
        Assertions.assertEquals(result.getRaceRecordDetail().getFurlong3fg(), "0:37.4");
        Assertions.assertEquals(result.getRaceRecordDetail().getFurlong1fg(), "0:13.2");
        Assertions.assertEquals(result.getRaceRecordDetail().getRaceRecord(), "1:01.7");
    }
}
