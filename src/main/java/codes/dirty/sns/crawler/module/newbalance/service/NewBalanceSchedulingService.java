package codes.dirty.sns.crawler.module.newbalance.service;

import codes.dirty.sns.crawler.common.config.DiscordProperty;
import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.common.util.ObjectMapperUtils;
import codes.dirty.sns.crawler.external.discord.DiscordRelayApi;
import codes.dirty.sns.crawler.module.newbalance.model.NewBalanceStock;
import codes.dirty.sns.crawler.module.newbalance.property.NewBalanceProperty;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewBalanceSchedulingService implements SchedulingService<NewBalanceStock> {

    private final NewBalanceProperty newBalanceProperty;
    private final DiscordRelayApi discordRelayApi;
    private final DiscordProperty discordProperty;

    public NewBalanceSchedulingService(NewBalanceProperty newBalanceProperty,
                                       DiscordRelayApi discordRelayApi,
                                       DiscordProperty discordProperty) {
        this.newBalanceProperty = newBalanceProperty;
        this.discordRelayApi = discordRelayApi;
        this.discordProperty = discordProperty;
    }

    @Override
    public List<NewBalanceStock> crawl() {
        try {
            // Get Page
            final Document doc = Jsoup.connect(newBalanceProperty.getUrl()).get();

            // Validate
            if (!doc.toString().contains("PROD_STOCK")) {
                log.error("Crawl failed. [{}]", "Stock info not found");
                throw new RuntimeException("Crawl failed. Stock info not found");
            }

            // Parse
            final String html = doc.toString();
            final String stockLineString = html.substring(html.indexOf("var PROD_STOCK"), html.indexOf("PROD_OPT_ICON"));
            final String stockJsonString = stockLineString.substring(stockLineString.indexOf("{"),
                                                                     stockLineString.indexOf("}") + 1);
            log.debug("stockJson: [{}]", stockJsonString);
            return ((Map<String, String>) ObjectMapperUtils.toObjectByJson(stockJsonString, Map.class))
                .entrySet()
                .stream()
                .map(entry -> NewBalanceStock.builder()
                                             .itemName(entry.getKey())
                                             .stock(Integer.parseInt(entry.getValue()))
                                             .build()
                )
                .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Crawl failed. [{}]", e);
            throw new RuntimeException("Crawl failed.", e);
        }
    }

    @Override
    public void handleCrawledResult(List<NewBalanceStock> newNewBalanceStocks) {
        if (newNewBalanceStocks.stream()
                               .anyMatch(e -> e.getItemName().contains(newBalanceProperty.getKeyword())
                                              && e.getStock() > 0)) {
            log.info("Target stock exists.");
            discordRelayApi.postDiscordRelayServer(discordProperty.getChannel(),
                                                   "Target stock [" + newBalanceProperty.getKeyword() + "] on URL [" +
                                                   newBalanceProperty.getUrl() + "] exists.");
        } else {
            log.info("Target stock does not exist.");
        }
    }
}
