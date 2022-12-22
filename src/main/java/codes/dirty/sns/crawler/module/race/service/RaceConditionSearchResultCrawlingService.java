package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.util.ObjectMapperUtils;
import codes.dirty.sns.crawler.common.util.RaceHtmlParseUtils;
import codes.dirty.sns.crawler.common.util.UrlUtils;
import codes.dirty.sns.crawler.module.race.model.RaceConditionSearchResult;
import codes.dirty.sns.crawler.module.race.model.RaceData;
import codes.dirty.sns.crawler.module.race.model.RaceResult;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RaceConditionSearchResultCrawlingService {
    // todo: configuration
    private final String baseUrl = "https://race.kra.co.kr";
    private final String detailUrl = "/raceScore/ScoretableDetailList.do";

    private RaceResultCrawlingService raceResultCrawlingService;

    public RaceConditionSearchResultCrawlingService(RaceResultCrawlingService raceResultCrawlingService) {
        this.raceResultCrawlingService = raceResultCrawlingService;
    }

    public List<RaceConditionSearchResult> parseConditionSearchResultDocument(Document doc) {
        List<RaceConditionSearchResult> results = doc.select("table.tableType2 > tbody > tr").stream().map(e -> {
                // to parse object.
                String detailPageFunctionCallString = e.select("td > a").attr("onclick");
                // parameter mean each index 0: meet, 1: date, 2: round
                String[] parameters = RaceHtmlParseUtils.parseJavascriptParamtersToArray(detailPageFunctionCallString);
                List<String> rowDatas = e.select("td").stream().map(row -> row.text()).collect(Collectors.toList());
                List<RaceResult> raceResultList = getRaceResultWithRequest(parameters);

                RaceConditionSearchResult result = RaceConditionSearchResult.builder()
                    .raceDate(parameters[1])
                    .round(parameters[2])
                    .category(rowDatas.get(1))
                    .grade(rowDatas.get(2))
                    .distance(rowDatas.get(3))
                    .courseCondition(rowDatas.get(4))
                    .weather(rowDatas.get(5))
                    .raceResultList(raceResultList)
                    .build();
                log.debug(ObjectMapperUtils.toJsonByObject(result));
                log.debug("\n");
                return result;
            })
            .collect(Collectors.toList());
        return results;
    }

    public List<RaceResult> getRaceResultWithRequest(String[] parameters) {
        String requestBody = UrlUtils.getUrlEncodedRequestBody(Map.of("meet", parameters[0], "realRcDate", parameters[1], "realRcNo", parameters[2]));
        try {
            Document doc = Jsoup.connect(baseUrl + detailUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .header("Host", "race.kra.co.kr")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Connection", "keep-alive")
                .header("Cache-Control", "max-age=0")
                .header("Accept-Encoding", "gzip, deflater")
                .header("Cache-Control", "max-age=0")
                .header("Content-Length", "95")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Sec-Fetch-User", "?1")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Origin", "https://race.kra.co.kr")
                .requestBody(requestBody)
                .post();

            log.debug("response result detail doc {}", doc.toString());

            return raceResultCrawlingService.parseHtmlForRaceResult(doc);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            String errMsg = String.format("[%s] Crawl failed. date=%s, raceNo=&s", this.getClass().getSimpleName(), parameters[1], parameters[2]);
            throw new RuntimeException(errMsg, e);
        }
    }
}
