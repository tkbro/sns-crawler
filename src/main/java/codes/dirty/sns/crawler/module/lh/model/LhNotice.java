package codes.dirty.sns.crawler.module.lh.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "lh")
@CompoundIndexes({
    @CompoundIndex(name = "code_id", def = "{'code' : 1, 'id': 1}", unique = true)
})
public class LhNotice {

    //There are two types in code : 02, 03 , the number of digits of the ID varies depending on the code. ex)2015122300008038, 0000059719
    private String code;
    private String id;

    private String category;
    private String title;
    private String region;
    private String startDate;
    private String endDate;
    private String status;
}
