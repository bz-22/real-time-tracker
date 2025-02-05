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
    private final String BASE_API_URL = "https://free-api-live-football-data.p.rapidapi.com";
    
    @Value("${rapidapi.key}")
    private String apiKey;

    @Value("${rapidapi.host}")
    private String apiHost;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getLiveScores() {
        String url = BASE_API_URL + "/football-get-all-matches-by-league?leagueid=42";
        return fetchDataFromAPI(url);
    }

    public String getLiveScoresById(String id) {
        String url = BASE_API_URL + "/football-get-all-matches-by-league?leagueid=" + id;  // Dynamic ID
        return fetchDataFromAPI(url);
    }

    private String fetchDataFromAPI(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
                return jsonNode.toString();  
            } catch (Exception e) {
                e.printStackTrace();
                return "Error parsing JSON: " + e.getMessage();
            }
        } else {
            return "Error: " + response.getStatusCode();
        }
    }
}

