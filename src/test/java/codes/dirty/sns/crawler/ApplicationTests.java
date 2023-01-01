package codes.dirty.sns.crawler;

import codes.dirty.sns.crawler.common.config.DiscordProperty;
import codes.dirty.sns.crawler.common.config.NoScheduleTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(NoScheduleTestConfiguration.class)
class ApplicationTests {

    @Autowired
    DiscordProperty discordProperty;

    @Test
    void contextLoads() {
    }

    @Test
    void discordPropertyTest() {
        assertNotNull(discordProperty.getEndpoint());
    }

}
