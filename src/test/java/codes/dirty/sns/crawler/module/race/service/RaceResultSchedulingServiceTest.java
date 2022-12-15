package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.module.race.model.RaceProfile;
import codes.dirty.sns.crawler.module.race.model.RaceRecord;
import codes.dirty.sns.crawler.module.race.model.RaceRecordResult;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RaceResultSchedulingServiceTest {
    private final String RESULT_SAMPLE = "RACE_result_sample.html";
    @Spy
    private RaceResultSchedulingService raceResultSchedulingService;

    @Test
    void parseTest() throws IOException {
        // given
        ClassLoader classLoader = getClass().getClassLoader();
        final Document doc = Jsoup.parse(new File(classLoader.getResource("html").getPath() + "\\" + RESULT_SAMPLE), null);

        // when
        List<RaceRecordResult> parseList = raceResultSchedulingService.parseHtmlForRaceResult(doc);
        RaceRecordResult result = parseList.stream()
            .sorted(Comparator.comparing(o -> o.getRaceResult().getRank()))
            .findFirst().orElseThrow();

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getRaceResult().getRank(), "1");
        Assertions.assertEquals(result.getRaceRecord().getRank(), "1");

        Assertions.assertEquals(result.getRaceResult().getBreeder(), "이신영");
        Assertions.assertEquals(result.getRaceResult().getWinRate(), "2.7");

        Assertions.assertEquals(result.getRaceRecord().getS1f(), "0:13.6");
        Assertions.assertEquals(result.getRaceRecord().getThirdCorner(), "0:13.6");
        Assertions.assertEquals(result.getRaceRecord().getG3f(), "0:24.3");
        Assertions.assertEquals(result.getRaceRecord().getFourthCorner(), "0:30.3");
        Assertions.assertEquals(result.getRaceRecord().getG1f(), "0:48.5");
        Assertions.assertEquals(result.getRaceRecord().getFurlong3fg(), "0:37.4");
        Assertions.assertEquals(result.getRaceRecord().getFurlong1fg(), "0:13.2");
        Assertions.assertEquals(result.getRaceRecord().getRaceRecord(), "1:01.7");
    }
}
