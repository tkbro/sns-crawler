package com.example.springtest.module.spotv.service;

import com.example.springtest.common.service.SchedulingService;
import com.example.springtest.common.util.UrlUtils;
import com.example.springtest.module.spotv.model.SpotvVideo;
import com.example.springtest.module.spotv.repository.SpotvMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

            driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
            Thread.sleep(500);
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
            Thread.sleep(500);
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
            Thread.sleep(500);
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
            Thread.sleep(500);
            driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
            Thread.sleep(500);

            Document doc = Jsoup.parse(driver.getPageSource(), "https://www.youtube.com");
            Elements contents = doc.select("ytd-grid-video-renderer.style-scope.ytd-grid-renderer");

            for (Element content : contents) {
                String title = content.select("#video-title.yt-simple-endpoint.style-scope.ytd-grid-video-renderer").text();
                String href = content.select("#video-title.yt-simple-endpoint.style-scope.ytd-grid-video-renderer").attr("abs:href");

                if (title.contains("")) {
                    URL url = new URL(href);
                    Map<String, String> queryMap = UrlUtils.getQueryMap(url.getQuery());

                    result.add(new SpotvVideo(queryMap.get("v"), href, title, Instant.now().toEpochMilli()));
                }
            }

        } catch (InterruptedException | MalformedURLException e) {
            log.error("Crawl failed. [{}]", e.getMessage());
            throw new RuntimeException("Crawl failed.", e);
        }
        return result;
    }

    @Override
    public void handleCrawledResult(List<SpotvVideo> crawledResult) {
        for (SpotvVideo result : crawledResult) {
            if (!spotvMongoRepository.existsByVideoId(result.getVideoId())) {
                log.info("Added new video: [{}]", result.getTitle());
                spotvMongoRepository.save(result);
            }
        }
    }

}

