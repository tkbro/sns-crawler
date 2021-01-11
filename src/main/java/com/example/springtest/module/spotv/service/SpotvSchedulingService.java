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
    private final String chromeDriverPath;

    public SpotvSchedulingService(SpotvMongoRepository spotvMongoRepository,
                                  @Value("${selenium.chrome-driver-path}") String chromeDriverPath) {
        this.spotvMongoRepository = spotvMongoRepository;
        this.chromeDriverPath = chromeDriverPath;
    }

    @Override
    public List<SpotvVideo> crawl() {
        final List<SpotvVideo> result = new ArrayList<>();
        try {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--headless");                 // Browser를 띄우지 않음
            options.addArguments("--disable-gpu");              // GPU를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
            options.addArguments("--no-sandbox");               // Sandbox 프로세스를 사용하지 않음, Linux에서 headless를 사용하는 경우 필요함.
            options.addArguments("--blink-settings=imagesEnabled=false");

            ChromeDriver driver = new ChromeDriver(options);

            driver.get("https://www.youtube.com/user/spotv/videos?view=0&sort=dd&flow=grid");

            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement parent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#items.style-scope.ytd-grid-renderer")));

            boolean isTimeout = false;
            //It scans 30 videos per loop, so it scans 3000 videos in total
            for(int i =  0; i < 100; i++) {
                Document doc = Jsoup.parse(parent.getAttribute("innerHTML"), "https://www.youtube.com");
                Elements contents = doc.select("ytd-grid-video-renderer.style-scope.ytd-grid-renderer");
                log.debug("Searched contents : {}", contents.size());

                //Basically it works 30 times, when it is not a multiple of 30, it only works until that point
                for(int j = 0; j < 30 && (i * 30) + j < contents.size(); j++) {
                    int videoNode = (i * 30) + j;
                    String href = contents.get(videoNode).select("#video-title.yt-simple-endpoint.style-scope.ytd-grid-video-renderer").attr("abs:href");

                    URL url = new URL(href);
                    Map<String, String> queryMap = UrlUtils.getQueryMap(url.getQuery());

                    if(!spotvMongoRepository.existsByVideoId(queryMap.get("v"))) {
                        String title = contents.get(videoNode).select("#video-title.yt-simple-endpoint.style-scope.ytd-grid-video-renderer").text();
                        result.add(new SpotvVideo(queryMap.get("v"), href, title, Instant.now().toEpochMilli()));
                        log.debug("node number : {}", videoNode);
                    }
                    else {
                        log.info("[Found existing video] New contents size : {}", result.size());
                        return result;
                    }
                }
                if(isTimeout) {
                    log.info("[There is no more video] New contents size : {}", result.size());
                    return result;
                }

                long start = System.currentTimeMillis();
                long endTime = start + (10 * 1000);     //Timeout is 10 sec

                //Send the "End key" until it finds the next 30 videos
                while (contents.size() < (30 * i) + 60) {
                    driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#items.style-scope.ytd-grid-renderer")));
                    doc = Jsoup.parse(parent.getAttribute("innerHTML"), "https://www.youtube.com");
                    contents = doc.select("ytd-grid-video-renderer.style-scope.ytd-grid-renderer");

                    if(System.currentTimeMillis() >= endTime) {
                        isTimeout = true;
                        break;
                    }
                }
            }

        } catch (MalformedURLException | TimeoutException e) {
            log.error("Crawl failed. [{}]", e.getMessage());
            throw new RuntimeException("Crawl failed.", e);
        }
        log.info("New contents size : {}", result.size());
        return result;
    }

    @Override
    public void handleCrawledResult(List<SpotvVideo> crawledResult) {
        for (SpotvVideo result : crawledResult) {
            log.debug("Added new video: [{}]", result.getTitle());
            spotvMongoRepository.save(result);
        }
        log.info("Added videos size : {}", crawledResult.size());
    }
}
