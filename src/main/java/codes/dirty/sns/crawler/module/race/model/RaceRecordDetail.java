package codes.dirty.sns.crawler.module.race.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RaceRecordDetail {
    private String rank;
    private String raceNo;

    private String totalSectionRank;
    private String s1f;
    private String firstCorner;
    private String secondCorner;
    private String thirdCorner;
    private String g3f;
    private String fourthCorner;
    private String g1f;
    private String furlong3fg;
    private String furlong1fg;
    private String raceRecord;
}
