package codes.dirty.sns.crawler.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spotv")
public class SpotvProperty {
    private String url;
    private int findPerLoop;
    private int maxFindVideo;
}
