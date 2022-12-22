package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.common.util.RaceHtmlParseUtils;
import codes.dirty.sns.crawler.module.race.model.RaceData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class RaceResultListSchedulingService implements SchedulingService<RaceData> {
    private final String baseUrl = "https://race.kra.co.kr";
    private final String detailUrl = "/raceScore/ScoretableDetailList.do";

    private final RaceResultCrawlingService raceResultCrawlingService;
    private final RaceProfileCrawlingService raceProfileCrawlingService;

    public RaceResultListSchedulingService(RaceResultCrawlingService raceResultCrawlingService,
                                           RaceProfileCrawlingService raceProfileCrawlingService) {
        this.raceResultCrawlingService = raceResultCrawlingService;
        this.raceProfileCrawlingService = raceProfileCrawlingService;
    }

    @Override
    public List<RaceData> crawl() {
        try {
            final Document doc = Jsoup.connect("test").get();

            // todo : validation (if need)

            return this.parseHtmlToRaceDataList(doc);
        } catch (IOException e) {
            log.error("Crawl failed. [{}]", e);
            throw new RuntimeException("Crawl failed.", e);
        }
    }

    public List<RaceData> parseHtmlToRaceDataList(Document doc) {
        List<Element> tableRowList = doc.select("div.tableType2 > table > tbody > tr");

        List<RaceData> raceDataList = new ArrayList<>();
        tableRowList.forEach(row -> {
            String href = row.select("td > p > a").first().attr("href");
            String[] parameters = RaceHtmlParseUtils.parseJavascriptParamtersToArray(href);
            String[] fieldNameList = new String[]{"meet", "realRcDate", "realRcNo"};
            StringBuilder sb = new StringBuilder();
            IntStream.range(0, parameters.length).forEach(i -> sb.append(String.format("%s=%s&", fieldNameList[i], parameters[i])));
            String requestBody = sb.substring(0, sb.length() - 1).toString();
            log.debug("req body = {}", requestBody);

            try {
                Document resultDetailDocument = Jsoup.connect(baseUrl + detailUrl)
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

                log.debug("detailDoc: {}", resultDetailDocument.toString());
                raceDataList.add(new RaceData(parameters[1], parameters[2], raceResultCrawlingService.parseHtmlForRaceResult(resultDetailDocument)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return raceDataList;
    }

    @Override
    public void handleCrawledResult(List<RaceData> crawledResult) {
    }
}
