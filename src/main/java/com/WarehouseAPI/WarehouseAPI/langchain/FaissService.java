package com.WarehouseAPI.WarehouseAPI.langchain;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class FaissService {
    private final RestTemplate restTemplate = new RestTemplate();

    public void indexVectors(List<List<Double>> vectors, List<String> ids) {
        Map<String, Object> payload = Map.of(
                "vectors", vectors,
                "ids", ids
        );
        restTemplate.postForEntity("http://localhost:8000/index", payload, String.class);
    }

    public List<String> search(List<Double> queryVec, int topK) {
        Map<String, Object> payload = Map.of(
                "query", List.of(queryVec),
                "top_k", topK
        );
        ResponseEntity<List> res = restTemplate.postForEntity("http://localhost:8000/search", payload, List.class);
        return res.getBody();
    }
}

