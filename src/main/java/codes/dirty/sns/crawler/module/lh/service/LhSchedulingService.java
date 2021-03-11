package codes.dirty.sns.crawler.module.lh.service;

import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.module.lh.model.LhNotice;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LhSchedulingService implements SchedulingService<LhNotice> {

    @Override
    public List<LhNotice> crawl() {
        final List<LhNotice> result = new ArrayList<>();

        try {
            Document doc = requestDocument();

            result.addAll(parseNotices(doc));

        } catch (IOException e) {
            log.error("Crawl failed. [{}]", e.getMessage());
            throw new RuntimeException("Crawl failed.", e);
        }

        for (LhNotice notice : result) {
            log.debug(notice.getCode() + ", " + notice.getId() + " " + notice.getCategory() + " / " + notice.getTitle());
            log.debug(notice.getRegion() + " / " + notice.getStartDate() + " ~ " + notice.getEndDate() + " / " + notice.getStatus());
            log.debug("--------------");
        }
        log.debug("{}", result.size());

        return result;
    }

    private List<LhNotice> parseNotices(Document doc) {
        Elements notices = doc.select("#dsList Rows Row");

        return notices.stream()
                      .map(notice -> {
                          final String code = notice.select("#CCR_CNNT_SYS_DS_CD").text();
                          final String id = notice.select("#PAN_ID").text();
                          final String category = notice.select("#AIS_TP_CD_NM").text();
                          final String title = notice.select("#PAN_NM").text();
                          final String region = notice.select("#CNP_CD_NM").text();
                          final String startDate = notice.select("#PAN_DT").text();
                          final String endDate = notice.select("#CLSG_DT").text();
                          final String status = notice.select("#PAN_SS").text();

                          return LhNotice.builder()
                                         .code(code)
                                         .id(id)
                                         .category(category)
                                         .title(title)
                                         .region(region)
                                         .startDate(startDate)
                                         .endDate(endDate)
                                         .status(status)
                                         .build();
                      })
                      .collect(Collectors.toList());
    }

    private Document requestDocument() throws IOException {
        return Jsoup.connect("https://apply.lh.or.kr/lhCmcNoSessionAdapter.lh?serviceID=OCMC_LCC_SIL_SILSNOT_L0001")
                    .userAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36")
                    .header("Host", "apply.lh.or.kr")
                    .header("Connection", "keep-alive")
                    .header("Content-Length", "1792")
                    .header("Pragma", "no-cache")
                    .header("Cache-Control", "no-cache, no-store")
                    .header("sec-ch-ua",
                            "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("Content-Type", "text/xml")
                    .header("Accept", "application/xml, text/xml, */*")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("If-Modified-Since", "Thu, 01 Jun 1970 00:00:00 GMT")
                    .header("Expires", "-1")
                    .header("Origin", "https://apply.lh.or.kr")
                    .header("Sec-Fetch-Site", "same-origin")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Referer", "https://apply.lh.or.kr/LH/index.html")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                    .requestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                     "<Root xmlns=\"http://www.nexacroplatform.com/platform/dataset\">\n" +
                                     "\t<Parameters>\n" +
                                     "\t\t<Parameter id=\"_fbp\"></Parameter>\n" +
                                     "\t\t<Parameter id=\"NetFunnel_ID\"></Parameter>\n" +
                                     "\t</Parameters>\n" +
                                     "\t<Dataset id=\"dsSch\">\n" +
                                     "\t\t<ColumnInfo>\n" +
                                     "\t\t\t<Column id=\"PAN_NM\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"CNP_CD\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"PG_SZ\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"PAGE\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"CS_CD\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"PAN_ST_DT\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"PAN_ED_DT\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"CLSG_ST_DT\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"CLSG_ED_DT\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"UPP_AIS_TP_CD\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"PAN_SS\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"AIS_TP_CD\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"PREVIEW\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"SCH_TY\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"SCH_ARA\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"SCH_PAN_SS\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"AIS_TP_CD_INT\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t\t<Column id=\"MVIN_QF\" type=\"STRING\" size=\"256\"  />\n" +
                                     "\t\t</ColumnInfo>\n" +
                                     "\t\t<Rows>\n" +
                                     "\t\t\t<Row>\n" +
                                     "\t\t\t\t<Col id=\"CNP_CD\" />\n" +
                                     "\t\t\t\t<Col id=\"PG_SZ\">50</Col>\n" +
                                     "\t\t\t\t<Col id=\"PAGE\">1</Col>\n" +
                                     "\t\t\t\t<Col id=\"CS_CD\">CNP_CD</Col>\n" +
                                     "\t\t\t\t<Col id=\"PAN_ST_DT\">20200101</Col>\n" +
                                     "\t\t\t\t<Col id=\"PAN_ED_DT\">20991231</Col>\n" +
                                     "\t\t\t\t<Col id=\"CLSG_ST_DT\" />\n" +
                                     "\t\t\t\t<Col id=\"CLSG_ED_DT\" />\n" +
                                     "\t\t\t\t<Col id=\"UPP_AIS_TP_CD\">05</Col>\n" +
                                     "\t\t\t\t<Col id=\"PAN_SS\" />\n" +
                                     "\t\t\t\t<Col id=\"AIS_TP_CD\" />\n" +
                                     "\t\t\t\t<Col id=\"PREVIEW\">N</Col>\n" +
                                     "\t\t\t\t<Col id=\"SCH_TY\">0</Col>\n" +
                                     "\t\t\t\t<Col id=\"SCH_ARA\">0</Col>\n" +
                                     "\t\t\t\t<Col id=\"SCH_PAN_SS\">0</Col>\n" +
                                     "\t\t\t\t<Col id=\"MVIN_QF\" />\n" +
                                     "\t\t\t</Row>\n" +
                                     "\t\t</Rows>\n" +
                                     "\t</Dataset>\n" +
                                     "</Root>")
                    .post();
    }

    @Override
    public void handleCrawledResult(List<LhNotice> crawledResult) {

    }
}
