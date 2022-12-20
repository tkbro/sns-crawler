package codes.dirty.sns.crawler.module.race.service;

import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.common.util.ObjectMapperUtils;
import codes.dirty.sns.crawler.module.race.model.RaceProfileDetailRequest;
import codes.dirty.sns.crawler.module.race.model.RaceRecordResult;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RaceResultListSchedulingService implements SchedulingService<RaceRecordResult> {
    private final String baseUrl = "https://race.kra.co.kr";
    private final String detailUrl = "/raceScore/ScoretableDetailList.do";

    @Override
    public List<RaceRecordResult> crawl() {
        try {
            final Document doc = Jsoup.connect("test").get();

            // todo : validation (if need)

            return this.parseHtml(doc);
        } catch (IOException e) {
            log.error("Crawl failed. [{}]", e);
            throw new RuntimeException("Crawl failed.", e);
        }
    }

    public List<RaceRecordResult> parseHtml(Document doc) {
        List<Element> tableRowList = doc.select("div.tableType2 > table > tbody > tr");
        tableRowList.forEach(row -> {
            String href = row.select("td > p > a").first().attr("href");
            int startInclusive = href.indexOf('(') + 1;
            int endExclusive = href.indexOf(')');
            String parameterString = href.substring(startInclusive, endExclusive);
            parameterString = parameterString.replaceAll("\'", "");
            String[] parameters = parameterString.split(",");
            log.debug(String.join(",", parameters));

            try {
//                String body = ObjectMapperUtils.toJsonByObject(new RaceProfileDetailRequest(parameters[0], parameters[1], parameters[2]));
                Document detailDoc = Jsoup.connect(baseUrl + detailUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .header("Host", "race.kra.co.kr")
                    .header("Connection", "keep-alive")
                    .header("Cache-Control", "max-age=0")
                    .header("Accept-Encoding", "gzip, deflater")
                    .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Cache-Control", "max-age=0")
                    .header("Content-Length", "95")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "same-origin")
                    .header("Sec-Fetch-User", "?1")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("Origin", "https://race.kra.co.kr")
                    .requestBody("meet=1&realRcDate=20221218&realRcNo=1")
                    .post();

                log.debug("detailDoc: {}", detailDoc.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return new ArrayList<>();
    }

    @Override
    public void handleCrawledResult(List<RaceRecordResult> crawledResult) {

    }
}
