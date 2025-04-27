package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.langchain.APIService;
import com.WarehouseAPI.WarehouseAPI.langchain.LangChainQueryService;
import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {


    @Autowired
    private ChatbotService chatbotService;

    @GetMapping("/query")
    public String queryApi(@RequestParam String question) {
        return chatbotService.handleUserQuery(question);
    }
}
