package org.AiDiary.embedding.impl;

import org.AiDiary.dto.request.OllamaEmbeddingsDto;
import org.AiDiary.embedding.EmbeddingCreateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Map;


@Service
public class EmbeddingCreateServiceImpl implements EmbeddingCreateService {

    private final RestTemplate restTemplate;

    @Value("${mlService.api.eUrl}")
    private String embeddingUrl;

    public EmbeddingCreateServiceImpl(@Qualifier("embeddingTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    @Override
    public float[] createEmbeddings(String query) {

        Map<String, Object> payload = Map.of(
                "model", "nomic-embed-text",
                "prompt", query
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(payload, headers);

        ResponseEntity<OllamaEmbeddingsDto> response =
                restTemplate.exchange(
                        embeddingUrl + "/api/embeddings",
                        HttpMethod.POST,
                        entity,
                        OllamaEmbeddingsDto.class
                );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "Embedding API error: " + response.getStatusCode()
            );
        }

        OllamaEmbeddingsDto body = response.getBody();

        if (body == null || body.getEmbedding() == null) {
            throw new IllegalStateException("Empty embedding response");
        }

        return body.getEmbedding();
    }

}
