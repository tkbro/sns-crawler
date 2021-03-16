package codes.dirty.sns.crawler.module.spotv.service;

import codes.dirty.sns.crawler.common.config.DiscordProperty;
import codes.dirty.sns.crawler.common.config.SpotvProperty;
import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.common.util.UrlUtils;
import codes.dirty.sns.crawler.external.discord.DiscordRelayApi;
import codes.dirty.sns.crawler.module.spotv.model.SpotvVideo;
import codes.dirty.sns.crawler.module.spotv.repository.SpotvMongoRepository;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotvSchedulingService implements SchedulingService<SpotvVideo> {

    private final SpotvMongoRepository spotvMongoRepository;
    private final ChromeDriver driver;
    private final SpotvProperty spotvProperty;
    private final DiscordRelayApi discordRelayApi;
    private final DiscordProperty discordProperty;

    public SpotvSchedulingService(SpotvMongoRepository spotvMongoRepository,
                                  @Value("${selenium.chrome-driver-path}") String chromeDriverPath,
                                  SpotvProperty spotvProperty,
                                  DiscordRelayApi discordRelayApi,
                                  DiscordProperty discordProperty) {
        this.spotvMongoRepository = spotvMongoRepository;
        this.driver = buildChromeDriver(chromeDriverPath);
        this.spotvProperty = spotvProperty;
        this.discordRelayApi = discordRelayApi;
        this.discordProperty = discordProperty;
    }

    private ChromeDriver buildChromeDriver(String chromeDriverPath) {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--headless");                 // Browser를 띄우지 않음
        options.addArguments("--disable-gpu");              // GPU를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        options.addArguments("--no-sandbox");               // Sandbox 프로세스를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        options.addArguments("--disable-dev-shm-usage");    // https://github.com/elgalu/docker-selenium/issues/20#issuecomment-407101358
        options.addArguments("--blink-settings=imagesEnabled=false");
        return new ChromeDriver(options);
    }

    @Override
    public List<SpotvVideo> crawl() {
        final List<SpotvVideo> result = new ArrayList<>();
        int videoNode = 0;
        boolean isNotTimeout;
        try {
            driver.get(spotvProperty.getUrl());

            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                "#contents.style-scope ytd-item-section-renderer")));
            if (videoExists(parent)) {
                parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                    "#items.style-scope.ytd-grid-renderer")));
            } else {
                log.info("[There is no video on this channel]");
                return result;
            }

            while (videoNode < spotvProperty.getMaxFindVideo()) {
                isNotTimeout = sendEndKey(videoNode, 30);
                Document doc = Jsoup.parse(parent.getAttribute("innerHTML"), "https://www.youtube.com");
                Elements contents = doc.select("ytd-grid-video-renderer.style-scope.ytd-grid-renderer");
                log.debug("Searched contents : {}", contents.size());

                while (videoNode < contents.size() && videoNode < spotvProperty.getMaxFindVideo()) {

                    String href = contents.get(videoNode)
                                          .select("#video-title.yt-simple-endpoint.style-scope.ytd-grid-video-renderer")
                                          .attr("abs:href");

                    if (spotvMongoRepository.existsByVideoId(extractVideoIdByURL(href))) {
                        log.info("[Stopped searching because found existing video] New contents size : {}",
                                 result.size());
                        return result;
                    } else {
                        String title = contents.get(videoNode)
                                               .select(
                                                   "#video-title.yt-simple-endpoint.style-scope.ytd-grid-video-renderer")
                                               .text();
                        result.add(new SpotvVideo(extractVideoIdByURL(href),
                                                  href,
                                                  title,
                                                  null));
                        log.debug("node number : {}", videoNode);
                    }
                    videoNode += 1;
                }
                if (!isNotTimeout) {
                    log.info("[No more videos found in the searching time] New contents size : {}", result.size());
                    return result;
                }
            }

        } catch (MalformedURLException | TimeoutException e) {
            log.error("Crawl failed. [{}]", e.getMessage());
            throw new RuntimeException("Crawl failed.", e);
        }
        log.info("[Found all to Maximum videos]New contents size : {}", result.size());
        return result;
    }

    @Override
    public void handleCrawledResult(List<SpotvVideo> crawledResult) {
        long currentEpochMilli = Instant.now().toEpochMilli();
        for (SpotvVideo video : crawledResult) {
            video.setCreatedAt(currentEpochMilli);
            currentEpochMilli--;
        }
        spotvMongoRepository.saveAll(crawledResult);
        log.info("Added videos size : {}", crawledResult.size());

        List<SpotvVideo> spotvVideosByKeyword = filterSpotvVideosByKeyword(crawledResult,
                                                                           discordProperty.getKeywords());
        if (spotvVideosByKeyword.isEmpty()) {
            log.info("[There is no video with keyword]");
        } else {
            discordRelayApi.postDiscordRelayServer(discordProperty.getChannel(),
                                                   formatRequestMsg(spotvVideosByKeyword));
        }
    }

    public List<SpotvVideo> filterSpotvVideosByKeyword(List<SpotvVideo> spotvVideos, List<String> keywords) {

        return spotvVideos.stream()
                          .filter(spotvVideo -> (keywords.stream()
                                                         .anyMatch(keyword -> spotvVideo.getTitle().contains(keyword))))
                          .collect(Collectors.toList());
    }

    public String extractVideoIdByURL(String href) throws MalformedURLException {
        URL url = new URL(href);
        Map<String, String> queryMap = UrlUtils.getQueryMap(url.getQuery());
        return queryMap.get("v");
    }

    public boolean videoExists(WebElement parent) {
        List<WebElement> videos = parent.findElements(By.cssSelector("#items.style-scope.ytd-grid-renderer"));
        if (videos.isEmpty()) {
            log.debug("Video does not exist");
            return false;
        } else {
            log.debug("Video exists on this channel");
            return true;
        }
    }

    /**
     * Send End Key into body and Find videos from {@code videoNode} to {@code videoNode + spotvProperty.getFindPerLoop}
     * while {@code timeOutInSeconds}.
     *
     * @return true if this method find all video nodes while @timeOutInSeconds
     */
    public boolean sendEndKey(int videoNode, long timeOutInSeconds) {
        long endTime = Instant.now().toEpochMilli() + (timeOutInSeconds * 1000);     //Timeout is timeOutInSeconds
        List<WebElement> contents;
        do {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                "#items.style-scope.ytd-grid-renderer")));
            contents = parent.findElements(By.cssSelector("ytd-grid-video-renderer.style-scope.ytd-grid-renderer"));

            if (Instant.now().toEpochMilli() >= endTime) {
                log.debug("[Time out] New found videos : {} ~ {}", videoNode, contents.size() - 1);
                return false;
            }
            log.debug("Waiting result by END key, time remaining : {}ms", endTime - System.currentTimeMillis());

        } while (contents.size() < videoNode + spotvProperty.getFindPerLoop());
        log.debug("New found videos : {} ~ {}", videoNode, contents.size() - 1);
        return true;
    }

    private String formatRequestMsg(List<SpotvVideo> spotvVideos) {
        StringBuilder msg = new StringBuilder();
        for (SpotvVideo video : spotvVideos) {
            if (msg.length() != 0) {
                msg.append(", ");
            }
            msg.append(video.getTitle())
               .append(" - ")
               .append(video.getUrl());
        }
        return msg.toString();
    }
}
