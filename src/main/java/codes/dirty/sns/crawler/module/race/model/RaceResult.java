package codes.dirty.sns.crawler.module.race.model;

import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RaceResult {
    private String rank;
    private String raceNo;

    private String name;
    private String country;
    private String sex;
    private String age;
    private String raceWeight;
    private String rating;
    private String rider;
    private String breeder;
    private String owner;
    private String recordDiff;
    private String horseWeight;
    private String winRate;
    private String placeRate;
    private String gearState;
}
