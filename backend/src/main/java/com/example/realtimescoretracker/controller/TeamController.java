package com.example.realtimescoretracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class TeamController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Endpoint to fetch teams for a specific league
    @GetMapping("/teams/{leagueId}")
    public List<Map<String, Object>> getTeamsByLeague(@PathVariable int leagueId) {
        // SQL query to fetch teams based on league_id
        String sql = "SELECT t.id AS team_id, t.name AS team_name, t.logoUrl " +
                     "FROM teams t " +
                     "JOIN team_leagues tl ON t.id = tl.team_id " +
                     "WHERE tl.league_id = ?";
        
        // Query the database with the leagueId as a parameter
        return jdbcTemplate.queryForList(sql, leagueId); // Return the result as a List of Maps
    }

    // Endpoint to fetch all teams (without filtering by league)
    @GetMapping("/teams")
    public List<Map<String, Object>> getAllTeams() {
        // SQL query to fetch all teams
        String sql = "SELECT * FROM teams";
        return jdbcTemplate.queryForList(sql); // Query the database and return the result as a List of Maps
    }
}
