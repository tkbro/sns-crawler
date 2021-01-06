package com.example.springtest.module.spotv.repository;

import com.example.springtest.module.spotv.model.SpotvVideo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpotvMongoRepository extends MongoRepository<SpotvVideo, String> {
    List<SpotvVideo> findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(Long createdAt);

    boolean existsByVideoId(String videoId);
}
