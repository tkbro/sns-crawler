package codes.dirty.sns.crawler.module.race.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RaceResultDetail {
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

    private String horseNo;
    private String riderNo;
    private String ownerNo;
    private String breederNo;
}
