package com.example.realtimescoretracker.controller;

import com.example.realtimescoretracker.service.FootballScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scores")
public class FootballScoreController {
    private final FootballScoreService footballScoreService;

    public FootballScoreController(FootballScoreService footballScoreService) {
        this.footballScoreService = footballScoreService;
    }

    @GetMapping("/fixtures")
    public String getLiveScores() {
        return footballScoreService.getLiveScores();
    }
}
