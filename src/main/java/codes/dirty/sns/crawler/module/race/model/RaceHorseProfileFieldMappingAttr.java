package codes.dirty.sns.crawler.module.race.model;

import codes.dirty.sns.crawler.module.race.service.RaceProfileCrawlingService;
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
public enum RaceHorseProfileFieldMappingAttr {

    NAME("name", RaceProfileCrawlingService.keyNameList.get(0)),
    RATING("rating", RaceProfileCrawlingService.keyNameList.get(1)),
    REG_NO("regNo", RaceProfileCrawlingService.keyNameList.get(2)),
    GRADE("grade", RaceProfileCrawlingService.keyNameList.get(3)),
    SEX("sex", RaceProfileCrawlingService.keyNameList.get(4)),
    HOMETOWN("hometown", RaceProfileCrawlingService.keyNameList.get(5)),
    TRAINER("trainer", RaceProfileCrawlingService.keyNameList.get(6)),
    BIRTH("birth", RaceProfileCrawlingService.keyNameList.get(7)),
    AGE("age", RaceProfileCrawlingService.keyNameList.get(8)),
    COLOR("color", RaceProfileCrawlingService.keyNameList.get(9)),
    OWNER("owner", RaceProfileCrawlingService.keyNameList.get(10)),
    CAREER("career", RaceProfileCrawlingService.keyNameList.get(11)),
    RETIRE("retire", RaceProfileCrawlingService.keyNameList.get(12)),
    PLAY_YN("playYn", RaceProfileCrawlingService.keyNameList.get(13)),
    BREEDER("breeder", RaceProfileCrawlingService.keyNameList.get(14)),
    TOTAL_RECORD("totalRecord", RaceProfileCrawlingService.keyNameList.get(15)),
    FATHER("father", RaceProfileCrawlingService.keyNameList.get(16)),
    MOTHER("mother", RaceProfileCrawlingService.keyNameList.get(17)),
    WIN_RATE("winRate", RaceProfileCrawlingService.keyNameList.get(18)),
    NOTE("note", RaceProfileCrawlingService.keyNameList.get(19)),
    TATTOO("tattoo", RaceProfileCrawlingService.keyNameList.get(20)),
    REG_DATE("regDate", RaceProfileCrawlingService.keyNameList.get(21)),
    FIRST_PRICE("firstPrice", RaceProfileCrawlingService.keyNameList.get(22)),
    RECENT_PRICE("recentPrice", RaceProfileCrawlingService.keyNameList.get(23)),
    TOTAL_PRIZE("totalPrize", RaceProfileCrawlingService.keyNameList.get(24)),
    RECENT_6_PRIZE("recent6Prize", RaceProfileCrawlingService.keyNameList.get(25)),
    RECENT_3_PRIZE("recent3Prize", RaceProfileCrawlingService.keyNameList.get(26)),
    ;

    private String propertyName;
    private String htmlFieldName;
}
