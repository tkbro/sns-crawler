package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.module.race.model.RaceHorseProfile;
import codes.dirty.sns.crawler.module.race.model.RaceHorseProfileFieldMappingAttr;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class RaceProfileCrawlingService implements SchedulingService<RaceHorseProfile> {
    public static List<String> keyNameList = List.of("마명", "레이팅", "마번", "등급", "성별", "산지", "조교사", "생년월일",
        "연령", "모색", "마주", "출전기간", "퇴역자마", "출전예정", "생산자", "통산전적", "부마", "모마", "승률", "특징", "낙인", "경주마등록",
        "최초도입가", "최근거래가", "수득상금", "최근6회\r수득상금", "최근3회\r수득상금");

    // todo : replace to race profile url
    @Override
    public List<RaceHorseProfile> crawl() {
        try {
            final Document doc = Jsoup.connect("test").get();

            // todo : validation (if need)

            return this.parseHtmlForHorseProfile(doc);
        } catch (IOException e) {
            log.error("Crawl failed. [{}]", e);
            throw new RuntimeException("Crawl failed.", e);
        }
    }

    public List<RaceHorseProfile> parseHtmlForHorseProfile(Document doc) {
        List<Element> horseProfileElementList = doc.select("div.tableType1 > table  > tbody > tr").not(".case");
        StringBuilder sb = new StringBuilder();
        Map<String, String> profileValueMap = new HashMap<>();
        for (Element element : horseProfileElementList) {
            IntStream.range(0, element.children().select("th").size()).forEach(index -> {
                String fieldName = element.children().select("th").get(index).text();
                String fieldValue = element.children().select("td").get(index).text();
                sb.append(String.format("field: %s, value: %s \n", fieldName, fieldValue));
                profileValueMap.put(fieldName, fieldValue);
            });
        }

        log.debug("horse base profile parse:" + sb);

        String noteField = doc.select("div.tableType1 > table  > tbody > tr.case > th").text();
        List<String> noteValues = doc.select("div.tableType1 > table  > tbody > tr.case > td").stream().map(e -> e.text()).collect(Collectors.toList());
        String noteContent = String.join(",", noteValues);
        profileValueMap.put(noteField, noteContent);

        log.debug("horse profile note info parse:" + noteContent);

        Arrays.stream(RaceHorseProfileFieldMappingAttr.values())
            .collect(Collectors.toMap(RaceHorseProfileFieldMappingAttr::getHtmlFieldName, RaceHorseProfileFieldMappingAttr::getPropertyName));
        RaceHorseProfile profile = RaceHorseProfile.builder()
            .name(profileValueMap.get(keyNameList.get(0)))
            .rating(profileValueMap.get(keyNameList.get(1)))
            .regNo(profileValueMap.get(keyNameList.get(2)))
            .grade(profileValueMap.get(keyNameList.get(3)))
            .sex(profileValueMap.get(keyNameList.get(4)))
            .hometown(profileValueMap.get(keyNameList.get(5)))
            .trainer(profileValueMap.get(keyNameList.get(6)))
            .birth(profileValueMap.get(keyNameList.get(7)))
            .age(profileValueMap.get(keyNameList.get(8)))
            .color(profileValueMap.get(keyNameList.get(9)))
            .owner(profileValueMap.get(keyNameList.get(10)))
            .career(profileValueMap.get(keyNameList.get(11)))
            .retire(profileValueMap.get(keyNameList.get(12)))
            .playYn(profileValueMap.get(keyNameList.get(13)))
            .breeder(profileValueMap.get(keyNameList.get(14)))
            .totalRecord(profileValueMap.get(keyNameList.get(15)))
            .father(profileValueMap.get(keyNameList.get(16)))
            .mother(profileValueMap.get(keyNameList.get(17)))
            .winRate(profileValueMap.get(keyNameList.get(18)))
            .note(profileValueMap.get(keyNameList.get(19)))
            .tattoo(profileValueMap.get(keyNameList.get(20)))
            .regDate(profileValueMap.get(keyNameList.get(21)))
            .firstPrice(profileValueMap.get(keyNameList.get(22)))
            .recentPrice(profileValueMap.get(keyNameList.get(23)))
            .totalPrize(profileValueMap.get(keyNameList.get(24)))
            .recent6Prize(profileValueMap.get(keyNameList.get(25)))
            .recent3Prize(profileValueMap.get(keyNameList.get(26)))
            .build();
        return List.of(profile);
    }

    @Override
    public void handleCrawledResult(List<RaceHorseProfile> crawledResult) {
    }
}
