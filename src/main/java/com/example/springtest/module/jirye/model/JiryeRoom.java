package com.example.springtest.module.jirye.model;

import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document(collection = "jiryeRoom")
@CompoundIndexes({
    @CompoundIndex(name = "bookingDate_roomName", def = "{'bookingDate' : -1, 'roomName': 1}", unique = true)
})
public class JiryeRoom {

    @Id
    private String id;

    /**
     * yyyyMMdd format
     */
    private Integer bookingDate;

    private String roomName;

    private boolean isBookable;

    private Long updatedAt;

    /**
     * e.g.: 2020-12-01 to 20201201
     */
    public static Integer formatBookingDate(LocalDate localDate) {
        return localDate.getYear() * 10000 +
               localDate.getMonthValue() * 100 +
               localDate.getDayOfMonth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.example.springtest.module.jirye.model.JiryeRoom that = (com.example.springtest.module.jirye.model.JiryeRoom) o;
        return Objects.equals(bookingDate, that.bookingDate) &&
               Objects.equals(roomName, that.roomName) &&
               Objects.equals(isBookable, that.isBookable);
    }
}
