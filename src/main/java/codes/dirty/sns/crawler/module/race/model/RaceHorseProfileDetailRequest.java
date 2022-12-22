package codes.dirty.sns.crawler.module.race.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RaceHorseProfileDetailRequest {
    private String meet; // 1
    private String realRcDate; // 20221218
    private String realRcNo; // 1 ~ maxNo each date
}
