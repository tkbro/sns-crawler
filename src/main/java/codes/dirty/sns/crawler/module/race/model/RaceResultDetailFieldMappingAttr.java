package codes.dirty.sns.crawler.module.race.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RaceResultDetailFieldMappingAttr {

    RANK("rank", "순위"),
    RACE_NO("raceNo", "마번"),

    NAME("name", "마명"),
    COUNTRY("country", "산지"),
    SEX("sex", "성별"),
    AGE("age", "연령"),
    RACE_WEIGHT("raceWeight", "중량"),
    RATING("rating", "레이팅"),
    RIDER("rider", "기수명"),
    BREEDER("breeder", "조교사명"),
    OWNER("owner", "마주명"),
    RECORD_DIFF("recordDiff", "도착차"),
    HORSE_WEIGHT("horseWeight", "마체중"),
    WIN_RATE("winRate", "단승"),
    PLACE_RATE("placeRate", "연승"),
    GEAR_STATE("gearState", "장구현황"),

    HORSE_NO("horseNo", "horseNo"),
    RIDER_NO("riderNo", "riderNo"),
    OWNER_NO("ownerNo", "ownerNo"),
    BREEDER_NO("breederNo", "breederNo"),
    ;

    private String propertyName;
    private String htmlFieldName;
}
