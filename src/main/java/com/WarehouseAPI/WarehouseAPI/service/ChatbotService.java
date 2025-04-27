package com.WarehouseAPI.WarehouseAPI.service;


import com.WarehouseAPI.WarehouseAPI.langchain.APIService;
import com.WarehouseAPI.WarehouseAPI.langchain.LangChainQueryService;
import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotService {

    @Autowired
    private LangChainQueryService langChainQueryService;

    @Autowired
    private APIService apiCallService;

    public String handleUserQuery(String userQuery) {
        String apiEndpoint = langChainQueryService.generateApiCall(userQuery);
        System.out.println("🎯 GPT tạo API Endpoint: " + apiEndpoint);

        return apiCallService.callApi(apiEndpoint);
    }
}
