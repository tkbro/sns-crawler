package com.example.springtest.module.spotv.service;

import com.example.springtest.common.service.SchedulingService;
import com.example.springtest.common.util.UrlUtils;
import com.example.springtest.module.spotv.model.SpotvVideo;
import com.example.springtest.module.spotv.repository.SpotvMongoRepository;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SpotvSchedulingService implements SchedulingService<SpotvVideo> {

    private final SpotvMongoRepository spotvMongoRepository;
    private final ChromeDriver driver;

    public SpotvSchedulingService(SpotvMongoRepository spotvMongoRepository,
                                  @Value("${selenium.chrome-driver-path}") String chromeDriverPath) {
        this.spotvMongoRepository = spotvMongoRepository;
        this.driver = buildChromeDriver(chromeDriverPath);
    }

    private ChromeDriver buildChromeDriver(String chromeDriverPath) {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--headless");                 // Browser를 띄우지 않음
        options.addArguments("--disable-gpu");              // GPU를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        options.addArguments("--no-sandbox");               // Sandbox 프로세스를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
        options.addArguments("--blink-settings=imagesEnabled=false");
        return new ChromeDriver(options);
    }

    @Override
    public List<SpotvVideo> crawl() {
        final List<SpotvVideo> result = new ArrayList<>();
        final int FIND_PER_LOOP = 60;
        final int MAX_FIND_VIDEO = 3000;
        int videoNode = 0;
        boolean isTimeout;
        try {
            driver.get("https://www.youtube.com/user/spotv/videos?view=0&sort=dd&flow=grid");

            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#contents.style-scope ytd-item-section-renderer")));
            if (!videoExists(parent)) {
                log.info("[There is no video on this channel]");
                return result;
            } else
                parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#items.style-scope.ytd-grid-renderer")));

            while (videoNode < MAX_FIND_VIDEO) {
                isTimeout = isTimeoutBySendEndKey(videoNode, FIND_PER_LOOP, 10);
                Document doc = Jsoup.parse(parent.getAttribute("innerHTML"), "https://www.youtube.com");
                Elements contents = doc.select("ytd-grid-video-renderer.style-scope.ytd-grid-renderer");
                log.debug("Searched contents : {}", contents.size());

                while (videoNode < contents.size() && videoNode < MAX_FIND_VIDEO) {

                    String href = contents.get(videoNode).select("#video-title.yt-simple-endpoint.style-scope.ytd-grid-video-renderer").attr("abs:href");

                    if (!spotvMongoRepository.existsByVideoId(toVideoIdByURL(href))) {
                        String title = contents.get(videoNode).select("#video-title.yt-simple-endpoint.style-scope.ytd-grid-video-renderer").text();
                        result.add(new SpotvVideo(toVideoIdByURL(href), href, title, Instant.now().toEpochMilli()));
                        log.debug("node number : {}", videoNode);
                    } else {
                        log.info("[Found existing video] New contents size : {}", result.size());
                        return result;
                    }
                    videoNode += 1;
                }
                if (isTimeout) {
                    log.info("[There is no more video] New contents size : {}", result.size());
                    return result;
                }
            }

        } catch (MalformedURLException | TimeoutException e) {
            log.error("Crawl failed. [{}]", e.getMessage());
            throw new RuntimeException("Crawl failed.", e);
        }
        log.info("[Found all to MAX_FIND_VIDEO]New contents size : {}", result.size());
        return result;
    }

    @Override
    public void handleCrawledResult(List<SpotvVideo> crawledResult) {
        spotvMongoRepository.saveAll(crawledResult);
        log.info("Added videos size : {}", crawledResult.size());
    }

    public String toVideoIdByURL(String href) throws MalformedURLException {
        URL url = new URL(href);
        Map<String, String> queryMap = UrlUtils.getQueryMap(url.getQuery());
        return queryMap.get("v");
    }

    public boolean videoExists(WebElement parent) {
        List<WebElement> videos = parent.findElements(By.cssSelector("#items.style-scope.ytd-grid-renderer"));
        if (!videos.isEmpty()) {
            log.debug("Video exists on this channel");
            return true;
        } else {
            log.debug("Video does not exist");
            return false;
        }
    }

    public boolean isTimeoutBySendEndKey(int videoNode, int FIND_PER_LOOP, long timeOutInSeconds) {
        long endTime = System.currentTimeMillis() + (timeOutInSeconds * 1000);     //Timeout is timeOutInSeconds
        List<WebElement> contents;
        do {
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#items.style-scope.ytd-grid-renderer")));
            contents = parent.findElements(By.cssSelector("ytd-grid-video-renderer.style-scope.ytd-grid-renderer"));

            if (System.currentTimeMillis() >= endTime) {
                log.debug("[Time out] New found videos : {} ~ {}", videoNode, contents.size() - 1);
                return true;
            }
        } while (contents.size() < videoNode + FIND_PER_LOOP);
        log.debug("New found videos : {} ~ {}", videoNode, contents.size() - 1);
        return false;
    }

}
