package codes.dirty.sns.crawler.module.race.model;

import codes.dirty.sns.crawler.module.race.service.RaceProfileSchedulingService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/***
 * 사용되지 않지만, keyNameList 노가다하고 field 명 매핑해둔게 아까워서 일단 남겨둠.
 * 완성이후에도 필요없으면 제거 예정
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RaceProfileFieldMappingAttr {

    NAME("name", RaceProfileSchedulingService.keyNameList.get(0)),
    RATING("rating", RaceProfileSchedulingService.keyNameList.get(1)),
    REG_NO("regNo", RaceProfileSchedulingService.keyNameList.get(2)),
    GRADE("grade", RaceProfileSchedulingService.keyNameList.get(3)),
    SEX("sex", RaceProfileSchedulingService.keyNameList.get(4)),
    HOMETOWN("hometown", RaceProfileSchedulingService.keyNameList.get(5)),
    TRAINER("trainer", RaceProfileSchedulingService.keyNameList.get(6)),
    BIRTH("birth", RaceProfileSchedulingService.keyNameList.get(7)),
    AGE("age", RaceProfileSchedulingService.keyNameList.get(8)),
    COLOR("color", RaceProfileSchedulingService.keyNameList.get(9)),
    OWNER("owner", RaceProfileSchedulingService.keyNameList.get(10)),
    CAREER("career", RaceProfileSchedulingService.keyNameList.get(11)),
    RETIRE("retire", RaceProfileSchedulingService.keyNameList.get(12)),
    PLAY_YN("playYn", RaceProfileSchedulingService.keyNameList.get(13)),
    BREEDER("breeder", RaceProfileSchedulingService.keyNameList.get(14)),
    TOTAL_RECORD("totalRecord", RaceProfileSchedulingService.keyNameList.get(15)),
    FATHER("father", RaceProfileSchedulingService.keyNameList.get(16)),
    MOTHER("mother", RaceProfileSchedulingService.keyNameList.get(17)),
    WIN_RATE("winRate", RaceProfileSchedulingService.keyNameList.get(18)),
    NOTE("note", RaceProfileSchedulingService.keyNameList.get(19)),
    TATTOO("tattoo", RaceProfileSchedulingService.keyNameList.get(20)),
    REG_DATE("regDate", RaceProfileSchedulingService.keyNameList.get(21)),
    FIRST_PRICE("firstPrice", RaceProfileSchedulingService.keyNameList.get(22)),
    RECENT_PRICE("recentPrice", RaceProfileSchedulingService.keyNameList.get(23)),
    TOTAL_PRIZE("totalPrize", RaceProfileSchedulingService.keyNameList.get(24)),
    RECENT_6_PRIZE("recent6Prize", RaceProfileSchedulingService.keyNameList.get(25)),
    RECENT_3_PRIZE("recent3Prize", RaceProfileSchedulingService.keyNameList.get(26)),
    ;

    private String propertyName;
    private String htmlFieldName;
}
