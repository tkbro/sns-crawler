package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.common.util.ObjectMapperUtils;
import codes.dirty.sns.crawler.common.util.UrlUtils;
import codes.dirty.sns.crawler.module.race.model.RaceConditionSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RaceConditionSearchResultSchedulingService implements SchedulingService<RaceConditionSearchResult> {
    // todo : configuration
    private final String originUrl = "https://race.kra.co.kr";
    private final String searchUrl = "/raceScore/ConditionScoreList2_1.do";

    public RaceConditionSearchResultCrawlingService raceConditionSearchResultCrawlingService;

    public RaceConditionSearchResultSchedulingService(RaceConditionSearchResultCrawlingService raceConditionSearchResultCrawlingService) {
        this.raceConditionSearchResultCrawlingService = raceConditionSearchResultCrawlingService;
    }

    @Override
    public List<RaceConditionSearchResult> crawl() {
        // 1. check last crawl date
        //      a.if last crawl date not exist
        //          1. find oldest date, can get from list url
        //          2. crawl oldest date, and record.
        //      b. if last crawl date eixst
        //          1. crawl next month, and record.
        return null;
    }

    public List<RaceConditionSearchResult> getSearchResultByYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int maxDayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate searchRangeStartDate = LocalDate.of(year, month, 1);
        LocalDate searchRangeEndDate = LocalDate.of(year, month, maxDayOfMonth);

        String requestBody = UrlUtils.getUrlEncodedRequestBody(
            Map.of("meet", "1", "sRaceStartDate", searchRangeStartDate.format(dateFormat), "sRaceEndDate", searchRangeEndDate.format(dateFormat)));
        requestBody += "&sHrNm=*&sHrNo=&sHrMeet=&sOwNm=*&sOwNo=&sJkName=&sTrNm=&sRcDist1=1000&sRcDist2=2300&sRcGrp1=1%B1%BA&sRcGrp2=%C8%A5OPEN&sBrdnWeight1=47&sBrdnWeight2=65&sWeather=&sTrackStatus=&sRHorseNum1=01&sRHorseNum2=16&sRaceType=&sBudam=&sOrder1=01&sOrder2=99&Act=04&Sub=2&code=0";

        log.debug(requestBody);
        try {
            Document searchListDocument = Jsoup.connect(originUrl + searchUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .header("Host", "race.kra.co.kr")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Connection", "keep-alive")
//                .header("Cache-Control", "max-age=0")
//                .header("Accept-Encoding", "gzip, deflater")
//                .header("Cache-Control", "max-age=0")
//                .header("Content-Length", "95")
//                .header("Sec-Fetch-Dest", "document")
//                .header("Sec-Fetch-Mode", "navigate")
//                .header("Sec-Fetch-Site", "same-origin")
//                .header("Sec-Fetch-User", "?1")
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .header("Origin", "https://race.kra.co.kr")
                .requestBody(requestBody)
                .post();

            log.debug(searchListDocument.toString());

            List<RaceConditionSearchResult> results =
                raceConditionSearchResultCrawlingService.parseConditionSearchResultDocument(searchListDocument);
            log.info(ObjectMapperUtils.toJsonByObject(results));

            return results;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            String errMsg = String.format("Crawl failed with year=%d month=%d", year, month);
            throw new RuntimeException(errMsg, e);
        }
    }

    @Override
    public void handleCrawledResult(List<RaceConditionSearchResult> crawledResult) {
    }
}
