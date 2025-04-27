package com.WarehouseAPI.WarehouseAPI.langchain;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIService {


    private final RestTemplate restTemplate = new RestTemplate();

    public String callApi(String apiUrl) {
        String fullUrl = "http://localhost:8081" + apiUrl; // Log URL đầy đủ
        System.out.println("🛠️ Gọi API: " + fullUrl);

        try {
            return restTemplate.getForObject(fullUrl, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "{ \"error\": \"Không thể gọi API: " + e.getMessage() + "\" }";
        }
    }

}
