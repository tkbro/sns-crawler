package codes.dirty.sns.crawler.module.race.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RaceRecordResult {
    private RaceResult raceResult;
    private RaceRecord raceRecord;
}
