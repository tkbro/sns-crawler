package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.module.race.model.RaceProfile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RaceResultListSchedulingServiceTest {
    private final String PROFILE_LIST_SAMPLE = "RACE_result_list_sample.html";

    @Spy
    private RaceResultListSchedulingService raceResultListSchedulingService;

    @Test
    void parseTest() throws IOException {
        // given
        ClassLoader classLoader = getClass().getClassLoader();
        final Document doc = Jsoup.parse(new File(classLoader.getResource("html").getPath() + "\\" + PROFILE_LIST_SAMPLE), null);

        // when
        raceResultListSchedulingService.parseHtml(doc);
//        RaceProfile profile = parseList.stream().findFirst().orElseThrow();

        // then
//        Assertions.assertNotNull(profile);
//        Assertions.assertEquals(profile.getName(), "아인슈페너 (EINSPANNER)");
//        Assertions.assertEquals(profile.getRegNo(), "0045785");
//        Assertions.assertEquals(profile.getAge(), "2");
//        Assertions.assertEquals(profile.getFirstPrice(), "55,000천원(경매)");
//        Assertions.assertEquals(profile.getTotalPrize(), "33,000,000원");
    }
}
