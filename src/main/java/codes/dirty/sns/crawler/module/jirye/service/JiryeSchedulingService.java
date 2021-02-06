package codes.dirty.sns.crawler.module.jirye.service;

import codes.dirty.sns.crawler.common.service.SchedulingService;
import codes.dirty.sns.crawler.module.jirye.model.JiryeRoom;
import codes.dirty.sns.crawler.module.jirye.model.JiryeRoomsDiffResult;
import codes.dirty.sns.crawler.module.jirye.model.JiryeRoomsDiffResult.JiryeRoomDiffResult;
import codes.dirty.sns.crawler.module.jirye.repository.JiryeRoomMongoRepository;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JiryeSchedulingService implements SchedulingService<JiryeRoom> {

    private final JiryeRoomMongoRepository jiryeRoomMongoRepository;

    public JiryeSchedulingService(JiryeRoomMongoRepository jiryeRoomMongoRepository) {
        this.jiryeRoomMongoRepository = jiryeRoomMongoRepository;
    }

    @Override
    public List<JiryeRoom> crawl() {
        final List<JiryeRoom> result = new ArrayList<>();
        try {
            final Document currentMonthDocument = requestPage();
            final LocalDate currentMonth = parseCurrentDateFromPage(currentMonthDocument);

            // crawl 3 months from current month
            // current month
            result.addAll(parseRooms(currentMonthDocument,
                                     currentMonth.getYear(),
                                     currentMonth.getMonthValue()));

            // next month
            final Document nextMonthDocument = requestPage(currentMonth.plusMonths(1).getYear(),
                                                           currentMonth.plusMonths(1).getMonthValue());
            result.addAll(parseRooms(nextMonthDocument,
                                     currentMonth.plusMonths(1).getYear(),
                                     currentMonth.plusMonths(1).getMonthValue()));

            // next of next month
            final Document nextOfNextMonthDocument = requestPage(currentMonth.plusMonths(2).getYear(),
                                                                 currentMonth.plusMonths(2).getMonthValue());
            result.addAll(parseRooms(nextOfNextMonthDocument,
                                     currentMonth.plusMonths(2).getYear(),
                                     currentMonth.plusMonths(2).getMonthValue()));

        } catch (IOException e) {
            log.error("Crawl failed. [{}]", e.getMessage());
            throw new RuntimeException("Crawl failed.", e);
        }
        return result;
    }

    @Override
    public void handleCrawledResult(List<JiryeRoom> newRooms) {
        final List<JiryeRoom> oldRooms = retrieveOldRooms(newRooms.stream()
                                                                  .map(JiryeRoom::getBookingDate)
                                                                  .collect(Collectors.toSet()));

        final JiryeRoomsDiffResult diffResult = diff(newRooms, oldRooms);

        jiryeRoomMongoRepository.saveAll(diffResult.getChangedNewRooms());
        log.info("Detected changes size: [{}], diff: [{}]", diffResult.getChangedNewRooms().size(), diffResult);
    }

    private List<JiryeRoom> retrieveOldRooms(Set<Integer> bookingDates) {
        return jiryeRoomMongoRepository.findAllByBookingDateIn(bookingDates);
    }

    /**
     * Diff all old and new rooms and set {@code id} on new room if needed. {@code id} will be used for {@code
     * MongoRepository.saveAll()}.
     */
    private JiryeRoomsDiffResult diff(List<JiryeRoom> newRooms, List<JiryeRoom> oldRooms) {
        final JiryeRoomsDiffResult result = new JiryeRoomsDiffResult();
        newRooms.forEach(newRoom -> {
            final AtomicBoolean isExistInBoth = new AtomicBoolean(false);
            oldRooms.forEach(oldRoom -> {
                if (newRoom.getBookingDate().equals(oldRoom.getBookingDate()) &&
                    newRoom.getRoomName().equals(oldRoom.getRoomName())) {

                    isExistInBoth.set(true);
                    if (!newRoom.equals(oldRoom)) {  // Exists in both new and old but different data
                        newRoom.setId(oldRoom.getId());
                        result.add(new JiryeRoomDiffResult(newRoom, oldRoom));
                    }
                }
            });
            if (!isExistInBoth.get()) {  // Only exists in newRooms
                result.add(new JiryeRoomDiffResult(newRoom));
            }
        });
        return result;
    }

    private Document requestPage() throws IOException {
        return Jsoup.connect("http://www.jirye.com/Book/booklist.php")
                    .get();
    }

    private Document requestPage(int year, int month) throws IOException {
        return Jsoup.connect("http://www.jirye.com/Book/booklist.php")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data("year", "" + year)
                    .data("month", "" + month)
                    .get();
    }

    private LocalDate parseCurrentDateFromPage(Document doc) {
        final int selectedYear = Integer.parseInt(doc.select("#calendar > div.tit > div.calMonth > form > " +
                                                             "select[name=year] > option[selected]")
                                                     .get(0)
                                                     .text());
        final int selectedMonth = Integer.parseInt(doc.select("#calendar > div.tit > div.calMonth > form > " +
                                                              "select[name=month] > option[selected]")
                                                      .get(0)
                                                      .text());
        return LocalDate.of(selectedYear, selectedMonth, 1);
    }

    private List<JiryeRoom> parseRooms(Document doc, int year, int month) {
        final List<JiryeRoom> result = new ArrayList<>();

        final List<Element> dates = doc.select("#fBookview > table > tbody > tr > td").stream()
                                       .filter(element -> element.childNodeSize() != 0)
                                       .collect(Collectors.toList());
        dates.forEach(date -> {
            final LocalDate currentDate = LocalDate.of(year,
                                                       month,
                                                       Integer.parseInt(date.child(0)
                                                                            .text()
                                                                            .trim()));
            result.addAll(date.children().stream()
                              .filter(room -> !"h1".equals(room.tagName()))  // remove title dayOfMonth
                              .map(room -> {
                                  final boolean isBookable = room.children()
                                                                 .stream()
                                                                 .anyMatch(e -> "a".equals(e.tagName()));
                                  final String roomName = isBookable ?
                                      room.child(1).text().trim() :
                                      room.childNode(1).toString().trim();

                                  return JiryeRoom.builder()
                                                  .bookingDate(JiryeRoom.formatBookingDate(currentDate))
                                                  .isBookable(isBookable)
                                                  .roomName(roomName)
                                                  .updatedAt(Instant.now().toEpochMilli())
                                                  .build();
                              })
                              .collect(Collectors.toList()));
        });
        return result;
    }
}
