package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.util.RaceHtmlParseUtils;
import codes.dirty.sns.crawler.module.race.model.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class RaceResultCrawlingService {
    public static List<String> keyNameOrderedList = List.of("순위", "마번", "S1F-1C-2C-3C-G3F-4C-G1F", "S1F 지점",
        "1코너 지점", "2코너 지점", "3코너 지점", "G3F 지점", "4코너 지점", "G1F 지점", "3F-G", "1F-G", "경주 기록");


    public List<RaceResult> parseHtmlForRaceResult(Document doc) {
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

            String horseParameterHref = child.select("td > a[href*=Horse]").attr("href");
            String[] horseDetailParameters = RaceHtmlParseUtils.parseJavascriptParamtersToArray(horseParameterHref);
            String horseNo = horseDetailParameters[0];
            parseMap.put(RaceResultDetailFieldMappingAttr.HORSE_NO.getHtmlFieldName(), horseNo);
            sb.append(String.format("key: %s, val: %s\n", RaceResultDetailFieldMappingAttr.HORSE_NO.getHtmlFieldName(), horseNo));
            String riderParameterHref = child.select("td > a[href*=Person]").attr("href");
            String[] riderDetailParameters = RaceHtmlParseUtils.parseJavascriptParamtersToArray(riderParameterHref);
            String riderNo = riderDetailParameters[0];
            parseMap.put(RaceResultDetailFieldMappingAttr.RIDER_NO.getHtmlFieldName(), riderNo);
            sb.append(String.format("key: %s, val: %s\n", RaceResultDetailFieldMappingAttr.RIDER_NO.getHtmlFieldName(), riderNo));
            String ownerParameterHref = child.select("td > a[href*=Owner]").attr("href");
            String[] ownerDetailParameters = RaceHtmlParseUtils.parseJavascriptParamtersToArray(ownerParameterHref);
            String ownerNo = ownerDetailParameters[0];
            sb.append(String.format("key: %s, val: %s\n", RaceResultDetailFieldMappingAttr.OWNER_NO.getHtmlFieldName(), ownerNo));
            parseMap.put(RaceResultDetailFieldMappingAttr.OWNER_NO.getHtmlFieldName(), ownerNo);
            String breederParameterHref = child.select("td > a[href*=Trainer]").attr("href");
            String[] breederDetailParameters = RaceHtmlParseUtils.parseJavascriptParamtersToArray(breederParameterHref);
            String breederNo = breederDetailParameters[0];
            sb.append(String.format("key: %s, val: %s\n", RaceResultDetailFieldMappingAttr.BREEDER_NO.getHtmlFieldName(), breederNo));
            parseMap.put(RaceResultDetailFieldMappingAttr.BREEDER_NO.getHtmlFieldName(), breederNo);
            resultMapList.add(parseMap);
        });

        Map<String, RaceResultDetail> raceResultMap = resultMapList.stream().map(map -> RaceResultDetail.builder()
                .rank(map.get(RaceResultDetailFieldMappingAttr.RANK.getHtmlFieldName()))
                .raceNo(map.get(RaceResultDetailFieldMappingAttr.RACE_NO.getHtmlFieldName()))
                .name(map.get(RaceResultDetailFieldMappingAttr.NAME.getHtmlFieldName()))
                .country(map.get(RaceResultDetailFieldMappingAttr.COUNTRY.getHtmlFieldName()))
                .sex(map.get(RaceResultDetailFieldMappingAttr.SEX.getHtmlFieldName()))
                .age(map.get(RaceResultDetailFieldMappingAttr.AGE.getHtmlFieldName()))
                .raceWeight(map.get(RaceResultDetailFieldMappingAttr.RACE_WEIGHT.getHtmlFieldName()))
                .rating(map.get(RaceResultDetailFieldMappingAttr.RATING.getHtmlFieldName()))
                .rider(map.get(RaceResultDetailFieldMappingAttr.RIDER.getHtmlFieldName()))
                .breeder(map.get(RaceResultDetailFieldMappingAttr.BREEDER.getHtmlFieldName()))
                .owner(map.get(RaceResultDetailFieldMappingAttr.OWNER.getHtmlFieldName()))
                .recordDiff(map.get(RaceResultDetailFieldMappingAttr.RECORD_DIFF.getHtmlFieldName()))
                .horseWeight(map.get(RaceResultDetailFieldMappingAttr.HORSE_WEIGHT.getHtmlFieldName()))
                .winRate(map.get(RaceResultDetailFieldMappingAttr.WIN_RATE.getHtmlFieldName()))
                .placeRate(map.get(RaceResultDetailFieldMappingAttr.PLACE_RATE.getHtmlFieldName()))
                .gearState(map.get(RaceResultDetailFieldMappingAttr.GEAR_STATE.getHtmlFieldName()))
                .horseNo(map.get(RaceResultDetailFieldMappingAttr.HORSE_NO.getHtmlFieldName()))
                .riderNo(map.get(RaceResultDetailFieldMappingAttr.RIDER_NO.getHtmlFieldName()))
                .ownerNo(map.get(RaceResultDetailFieldMappingAttr.OWNER_NO.getHtmlFieldName()))
                .breederNo(map.get(RaceResultDetailFieldMappingAttr.BREEDER_NO.getHtmlFieldName()))
                .build())
            .collect(Collectors.toMap(RaceResultDetail::getRaceNo, Function.identity()));

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
        Map<String, RaceRecordDetail> raceRecordMap = resultMapList.stream().map(map -> RaceRecordDetail.builder()
                .rank(map.get(RaceRecordDetailFieldMappingAttr.RANK.getHtmlFieldName()))
                .raceNo(map.get(RaceRecordDetailFieldMappingAttr.RACE_NO.getHtmlFieldName()))
                .totalSectionRank(map.get(RaceRecordDetailFieldMappingAttr.S1F_1C_2C_3C_G3F_4C_G1F.getHtmlFieldName()))
                .s1f(map.get(RaceRecordDetailFieldMappingAttr.S1F.getHtmlFieldName()))
                .firstCorner(map.get(RaceRecordDetailFieldMappingAttr.FIRST_CORNER.getHtmlFieldName()))
                .secondCorner(map.get(RaceRecordDetailFieldMappingAttr.SECOND_CORNER.getHtmlFieldName()))
                .thirdCorner(map.get(RaceRecordDetailFieldMappingAttr.THIRD_CORNER.getHtmlFieldName()))
                .g3f(map.get(RaceRecordDetailFieldMappingAttr.G3F.getHtmlFieldName()))
                .fourthCorner(map.get(RaceRecordDetailFieldMappingAttr.FOURTH_CORNER.getHtmlFieldName()))
                .g1f(map.get(RaceRecordDetailFieldMappingAttr.G1F.getHtmlFieldName()))
                .furlong3fg(map.get(RaceRecordDetailFieldMappingAttr.FURLONG_3F_G.getHtmlFieldName()))
                .furlong1fg(map.get(RaceRecordDetailFieldMappingAttr.FURLONG_1F_G.getHtmlFieldName()))
                .raceRecord(map.get(RaceRecordDetailFieldMappingAttr.RACE_RECORD.getHtmlFieldName()))
                .build())
            .collect(Collectors.toMap(RaceRecordDetail::getRaceNo, Function.identity()));

        log.debug("rank with record parse" + sb);

        Set<String> keySet = new HashSet<>();
        keySet.addAll(raceResultMap.keySet());
        keySet.addAll(raceRecordMap.keySet());
        return keySet.stream().map(key -> new RaceResult(raceResultMap.get(key), raceRecordMap.get(key))).collect(Collectors.toList());
    }
}
