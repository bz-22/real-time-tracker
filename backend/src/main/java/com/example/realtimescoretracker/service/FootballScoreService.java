package com.example.realtimescoretracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@Service
public class FootballScoreService {
    private final String API_URL = "https://free-api-live-football-data.p.rapidapi.com/football-get-all-matches-by-league?leagueid=42";
    @Value("${rapidapi.key}")
    private String apiKey;

    @Value("${rapidapi.host}")
    private String apiHost;

    public String getLiveScores() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, org.springframework.http.HttpMethod.GET, entity, String.class);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                // Attempt to parse the response as JSON
                JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
                return jsonNode.toString();  // Convert the JSON back to a string for display
            } catch (Exception e) {
                e.printStackTrace();
                return "Error parsing JSON: " + e.getMessage();
            }
        } else {
            return "Error: " + response.getStatusCode();
        }
    }
}
