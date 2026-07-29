package com.WarehouseAPI.WarehouseAPI.embeddings;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {
    @Value("${openai.api-key}")
    private String apiKey;

    public List<Double> getEmbedding(String input) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/embeddings";

        Map<String, Object> request = Map.of(
                "input", input,
                "model", "text-embedding-3-small"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        Map response = restTemplate.postForObject(url, entity, Map.class);

        return (List<Double>) ((Map) ((List) response.get("data")).get(0)).get("embedding");
    }
}
