package com.example.realtimescoretracker.controller;

import com.example.realtimescoretracker.service.FootballScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scores")
public class FootballScoreController {
    private final FootballScoreService footballScoreService;

    public FootballScoreController(FootballScoreService footballScoreService) {
        this.footballScoreService = footballScoreService;
    }

    @GetMapping("/fixtures/{id}")  // Accept an ID as part of the URL
    public String getLiveScores(@PathVariable String id) {
        return footballScoreService.getLiveScoresById(id); // Call the service method with the ID
    }
}

