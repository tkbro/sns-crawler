package codes.dirty.sns.crawler.module.race.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RaceConditionSearchResult {
    private String raceDate;
    private String round;
    private String category;
    private String grade;
    private String distance;
    private String courseCondition;
    private String weather;

    private List<RaceResult> raceResultList;
}
