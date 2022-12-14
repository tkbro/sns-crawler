package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.module.race.model.RaceResult;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class RaceResultSchedulingService implements SchedulingService<RaceResult> {
    @Override
    public List<RaceResult> crawl() {
        try {
            final Document doc = Jsoup.connect("test").get();

            // todo : validation (if need)

            return this.parseHtmlForRaceResult(doc);
        } catch (IOException e) {
            log.error("Crawl failed. [{}]", e);
            throw new RuntimeException("Crawl failed.", e);
        }
    }

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
            resultMapList.add(parseMap);
        });

        log.debug("rank with profile parse" + sb);
        // todo: instantiate object with resultMap
        sb.setLength(0);
        resultMapList.clear();

        Element rankTableWithRecord = resultElementTableList.get(1);
        List<String> rankTableWithRecordFieldNameList = rankTableWithRecord
            .select("th").stream().map(e -> e.text()).collect(Collectors.toList());
        rankTableWithRecord.select("tbody > tr").forEach(child -> {
            List<Element> valueElementList = child.select("td");
            Map<String, String> parseMap = new HashMap<>();
            IntStream.range(0, valueElementList.size()).forEach(i -> {
                String key = rankTableWithRecordFieldNameList.get(i);
                String val = valueElementList.get(i).text();
                parseMap.put(key, val);
                sb.append(String.format("key: %s, val: %s\n", key, val));
            });
            resultMapList.add(parseMap);
        });

        log.debug("rank with record parse" + sb);
        return new ArrayList();
    }

    @Override
    public void handleCrawledResult(List<RaceResult> crawledResult) {

    }
}
