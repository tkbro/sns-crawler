package codes.dirty.sns.crawler.module.race.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RaceData {
    private String date;
    private String raceIndex;
    private List<RaceResult> raceResultList;
}
