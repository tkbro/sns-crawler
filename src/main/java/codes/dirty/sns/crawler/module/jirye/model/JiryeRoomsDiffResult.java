package codes.dirty.sns.crawler.module.jirye.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Container object of diff results for rooms
 */
@NoArgsConstructor
@Getter
@ToString
public class JiryeRoomsDiffResult {

    private final List<JiryeRoomDiffResult> roomDiffResults = new ArrayList<>();

    public void add(JiryeRoomDiffResult roomDiffResult) {
        roomDiffResults.add(roomDiffResult);
    }

    public List<JiryeRoom> getChangedNewRooms() {
        return roomDiffResults.stream()
                              .map(JiryeRoomDiffResult::getNewRoom)
                              .collect(Collectors.toList());
    }

    @Getter
    @ToString
    public static class JiryeRoomDiffResult {

        private final JiryeRoom newRoom;
        private final JiryeRoom oldRoom;
        private final JiryeRoomDiffType diffType;

        public JiryeRoomDiffResult(JiryeRoom newRoom, JiryeRoom oldRoom) {
            this.newRoom = newRoom;
            this.oldRoom = oldRoom;
            this.diffType = calcDiffResultType(newRoom, oldRoom);
        }

        public JiryeRoomDiffResult(JiryeRoom newRoom) {
            this.newRoom = newRoom;
            this.oldRoom = null;
            this.diffType = calcDiffResultType(newRoom, null);
        }

        private JiryeRoomDiffType calcDiffResultType(JiryeRoom newRoom, JiryeRoom oldRoom) {
            if (newRoom.isBookable() && oldRoom == null) {
                return JiryeRoomDiffType.NEW_AVAILABLE_ROOM_DETECTED;
            } else if (!newRoom.isBookable() && oldRoom == null) {
                return JiryeRoomDiffType.NEW_NOT_AVAILABLE_ROOM_DETECTED;
            } else if (newRoom.isBookable() && !oldRoom.isBookable()) {
                return JiryeRoomDiffType.CHANGED_INTO_AVAILABLE_ROOM;
            } else if (!newRoom.isBookable() && oldRoom.isBookable()) {
                return JiryeRoomDiffType.CHANGED_INTO_NOT_AVAILABLE_ROOM;
            } else {
                throw new RuntimeException("Not changed. Must not reach here.");
            }
        }
    }

    public enum JiryeRoomDiffType {
        NEW_AVAILABLE_ROOM_DETECTED,
        NEW_NOT_AVAILABLE_ROOM_DETECTED,
        CHANGED_INTO_AVAILABLE_ROOM,
        CHANGED_INTO_NOT_AVAILABLE_ROOM
    }
}
