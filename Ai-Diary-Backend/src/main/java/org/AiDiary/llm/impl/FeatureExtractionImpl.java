package org.AiDiary.llm.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.AiDiary.llm.FeatureExtraction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class FeatureExtractionImpl implements FeatureExtraction {

    private final RestTemplate restTemplate;

    public FeatureExtractionImpl(@Qualifier("restTemplate")RestTemplate template){
        restTemplate=template;
    }

    @Value("${mlService.api.url}")
    private String apiUrl;

    private static final String content = """
            You are a text analyzer. Given a user's daily task or text, extract the following information in JSON format ONLY:
            
              1. Sentiment: Analyze the user's text and provide a sentiment score between 0 and 1. Example:
                 - Very Happy: 0.7
                 - Happy: 0.5
                 - Intermediate: 0.3
                 - Bad: 0.2
                 - So Bad:0.1
                 Use your judgment for other scores and assign a single sentiment_score.
            
              2. Features:
                 - Places: List all place names mentioned in the text (empty list if none).
                 - People: List all people mentioned in the text (empty list if none).
                 - Keywords: Extract between 1 and 5 important keywords from the text.
            
              The output must be valid JSON ONLY in the following structure:
            
              {
                  "sentiment_score": <float>,
                  "features": {
                      "places": [<string>, ...],
                      "people": [<string>, ...],
                      "keyword": [<string>, ...]
                  }
              }
            
              Example Input:
              "Today I went to Central Park with John and had a great picnic."
            
              Example Output:
              {
                  "sentiment_score": 0.7,
                  "features": {
                      "places": ["Central Park"],
                      "people": ["John"],
                      "keyword": ["picnic", "park", "outdoors"]
                  }
              }
            
              Now, analyze this text:
            """;

    @Override
    public String featureExtractor(String userMessage) {

        Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "always respond with exactly one json object. No extra text"
                        ),
                        Map.of(
                                "role", "user",
                                "content", content + " " + userMessage
                        )
                ),
                "model", "llama-3.3-70b-versatile",
                "temperature", 1,
                "max_completion_tokens", 1024,
                "top_p", 1,
                "stream", false
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                JsonNode.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("ML API error: " + response.getStatusCode());
        }

        JsonNode body = response.getBody();

        return body.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText();
    }
}
