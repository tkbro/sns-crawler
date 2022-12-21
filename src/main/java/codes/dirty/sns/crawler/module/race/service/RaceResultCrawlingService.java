package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.module.race.model.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class RaceResultCrawlingService {
    public static List<String> keyNameOrderedList = List.of("순위", "마번", "S1F-1C-2C-3C-G3F-4C-G1F", "S1F 지점",
        "1코너 지점", "2코너 지점", "3코너 지점", "G3F 지점", "4코너 지점", "G1F 지점", "3F-G", "1F-G", "경주 기록");


    public List<RaceRecordResult> parseHtmlForRaceResult(Document doc) {
        List<Element> resultElementTableList = doc.select("div.tableType2 > table");
        Element rankTableWithHorseProfile = resultElementTableList.get(0);
        List<String> rankTableWithProfileFieldNameList = rankTableWithHorseProfile
            .select("th").stream().map(e -> e.text()).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        List<Map<String, String>> resultMapList = new ArrayList<>();
        rankTableWithHorseProfile.select("tbody > tr").forEach(child -> {
            List<Element> valueElementList = child.select("td");
            Map<String, String> parseMap = new HashMap<>();
            IntStream.range(0, valueElementList.size()).forEach(i -> {
                String key = rankTableWithProfileFieldNameList.get(i);
                String val = valueElementList.get(i).text();
                parseMap.put(key, val);
                sb.append(String.format("key: %s, val: %s\n", key, val));
            });
            resultMapList.add(parseMap);
        });

        Map<String, RaceResult> raceResultMap = resultMapList.stream().map(map -> RaceResult.builder()
                .rank(map.get(RaceResultFieldMappingAttr.RANK.getHtmlFieldName()))
                .raceNo(map.get(RaceResultFieldMappingAttr.RACE_NO.getHtmlFieldName()))
                .name(map.get(RaceResultFieldMappingAttr.NAME.getHtmlFieldName()))
                .country(map.get(RaceResultFieldMappingAttr.COUNTRY.getHtmlFieldName()))
                .sex(map.get(RaceResultFieldMappingAttr.SEX.getHtmlFieldName()))
                .age(map.get(RaceResultFieldMappingAttr.AGE.getHtmlFieldName()))
                .raceWeight(map.get(RaceResultFieldMappingAttr.RACE_WEIGHT.getHtmlFieldName()))
                .rating(map.get(RaceResultFieldMappingAttr.RATING.getHtmlFieldName()))
                .rider(map.get(RaceResultFieldMappingAttr.RIDER.getHtmlFieldName()))
                .breeder(map.get(RaceResultFieldMappingAttr.BREEDER.getHtmlFieldName()))
                .owner(map.get(RaceResultFieldMappingAttr.OWNER.getHtmlFieldName()))
                .recordDiff(map.get(RaceResultFieldMappingAttr.RECORD_DIFF.getHtmlFieldName()))
                .horseWeight(map.get(RaceResultFieldMappingAttr.HORSE_WEIGHT.getHtmlFieldName()))
                .winRate(map.get(RaceResultFieldMappingAttr.WIN_RATE.getHtmlFieldName()))
                .placeRate(map.get(RaceResultFieldMappingAttr.PLACE_RATE.getHtmlFieldName()))
                .gearState(map.get(RaceResultFieldMappingAttr.GEAR_STATE.getHtmlFieldName()))
                .build())
            .collect(Collectors.toMap(RaceResult::getRaceNo, Function.identity()));

        log.debug("rank with profile parse" + sb);

        sb.setLength(0);
        resultMapList.clear();

        Element rankTableWithRecord = resultElementTableList.get(1);
        List<String> rankTableWithRecordFieldNameList = rankTableWithRecord
            .select("th").not("[scope=colgroup]").stream().map(e -> e.text()).collect(Collectors.toList());
        assert new HashSet<>(rankTableWithRecordFieldNameList).equals(new HashSet<>(keyNameOrderedList));

        rankTableWithRecord.select("tbody > tr").forEach(child -> {
            List<Element> valueElementList = child.select("td");
            Map<String, String> parseMap = new HashMap<>();
            IntStream.range(0, valueElementList.size()).forEach(i -> {
                String key = keyNameOrderedList.get(i);
                String val = valueElementList.get(i).text();
                parseMap.put(key, val);
                sb.append(String.format("key: %s, val: %s\n", key, val));
            });
            resultMapList.add(parseMap);
        });
        Map<String, RaceRecord> raceRecordMap = resultMapList.stream().map(map -> RaceRecord.builder()
                .rank(map.get(RaceRecordFieldMappingAttr.RANK.getHtmlFieldName()))
                .raceNo(map.get(RaceRecordFieldMappingAttr.RACE_NO.getHtmlFieldName()))
                .totalSectionRank(map.get(RaceRecordFieldMappingAttr.S1F_1C_2C_3C_G3F_4C_G1F.getHtmlFieldName()))
                .s1f(map.get(RaceRecordFieldMappingAttr.S1F.getHtmlFieldName()))
                .firstCorner(map.get(RaceRecordFieldMappingAttr.FIRST_CORNER.getHtmlFieldName()))
                .secondCorner(map.get(RaceRecordFieldMappingAttr.SECOND_CORNER.getHtmlFieldName()))
                .thirdCorner(map.get(RaceRecordFieldMappingAttr.THIRD_CORNER.getHtmlFieldName()))
                .g3f(map.get(RaceRecordFieldMappingAttr.G3F.getHtmlFieldName()))
                .fourthCorner(map.get(RaceRecordFieldMappingAttr.FOURTH_CORNER.getHtmlFieldName()))
                .g1f(map.get(RaceRecordFieldMappingAttr.G1F.getHtmlFieldName()))
                .furlong3fg(map.get(RaceRecordFieldMappingAttr.FURLONG_3F_G.getHtmlFieldName()))
                .furlong1fg(map.get(RaceRecordFieldMappingAttr.FURLONG_1F_G.getHtmlFieldName()))
                .raceRecord(map.get(RaceRecordFieldMappingAttr.RACE_RECORD.getHtmlFieldName()))
                .build())
            .collect(Collectors.toMap(RaceRecord::getRaceNo, Function.identity()));

        log.debug("rank with record parse" + sb);

        Set<String> keySet = new HashSet<>();
        keySet.addAll(raceResultMap.keySet());
        keySet.addAll(raceRecordMap.keySet());
        return keySet.stream().map(key -> new RaceRecordResult(raceResultMap.get(key), raceRecordMap.get(key))).collect(Collectors.toList());
    }
}
