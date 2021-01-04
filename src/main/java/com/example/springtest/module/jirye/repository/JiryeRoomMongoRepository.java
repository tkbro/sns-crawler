package com.example.springtest.module.jirye.repository;

import com.example.springtest.module.jirye.model.JiryeRoom;
import java.util.List;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JiryeRoomMongoRepository extends MongoRepository<JiryeRoom, String> {

    List<JiryeRoom> findAllByBookingDateIn(Set<Integer> bookingDates);
}
