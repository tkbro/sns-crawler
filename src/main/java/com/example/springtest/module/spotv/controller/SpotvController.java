package com.example.springtest.module.spotv.controller;

import com.example.springtest.module.spotv.repository.SpotvMongoRepository;
import com.example.springtest.module.spotv.model.SpotvVideo;
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
