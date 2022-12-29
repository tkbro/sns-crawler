package codes.dirty.sns.crawler.module.race.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RaceRecordDetailFieldMappingAttr {

    RANK("rank", "순위"),
    RACE_NO("raceNo", "마번"),

    S1F_1C_2C_3C_G3F_4C_G1F("totalSectionRank", "S1F-1C-2C-3C-G3F-4C-G1F"),
    S1F("s1f", "S1F 지점"), // 출발후 200 m 구간,
    FIRST_CORNER("firstCorner", "1코너 지점"),
    SECOND_CORNER("secondCorner", "2코너 지점"),
    THIRD_CORNER("thirdCorner", "3코너 지점"),
    G3F("g3f", "G3F 지점"), // 골인 600m 전방
    FOURTH_CORNER("fourthCorner", "4코너 지점"),
    G1F("g1f", "G1F 지점"),
    FURLONG_3F_G("furlong3fg", "3F-G"),
    FURLONG_1F_G("furlong1fg", "1F-G"),
    RACE_RECORD("raceRecord", "경주 기록")
    ;

    private String propertyName;
    private String htmlFieldName;
}
