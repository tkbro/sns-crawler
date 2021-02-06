package codes.dirty.sns.crawler.common.service;

import java.util.List;

/**
 * Common interface for creating scheduling tasks. The implementations should be executed by {@code
 * SchedulingConfiguration}.
 */
public interface SchedulingService<T> {

    /**
     * Crawl data from external source(e.g.: Web site).
     */
    List<T> crawl();

    /**
     * Do actions with crawled data.
     */
    void handleCrawledResult(List<T> crawledResult);
}
