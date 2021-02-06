package codes.dirty.sns.crawler.module.spotv.controller;

import codes.dirty.sns.crawler.module.spotv.model.SpotvVideo;
import codes.dirty.sns.crawler.module.spotv.repository.SpotvMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpotvController {

    @Autowired
    SpotvMongoRepository spotvMongoRepository;

    @GetMapping("/videos")
    public List<SpotvVideo> getVideos(@RequestParam long from){
        return spotvMongoRepository.findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(from);
    }

    @PostMapping("/videos")
    public ResponseEntity<SpotvVideo> postVideos(@RequestBody SpotvVideo spotvVideo) {
        spotvMongoRepository.save(spotvVideo);
        return new ResponseEntity<>(spotvVideo, HttpStatus.CREATED);
    }
}
