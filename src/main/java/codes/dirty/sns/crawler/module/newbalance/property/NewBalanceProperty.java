package codes.dirty.sns.crawler.module.newbalance.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "new-balance")
public class NewBalanceProperty {
    private String url;
    private String keyword;
    private int interval;
}
