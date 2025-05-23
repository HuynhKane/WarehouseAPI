package com.WarehouseAPI.WarehouseAPI.langchain;

import com.WarehouseAPI.WarehouseAPI.dto.ChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
public class RAGController {

    @Autowired
    private RAGPipeline ragService;

    @PostMapping("/index")
    public String indexAllProducts() {
        ragService.indexAll();
        return "Indexing completed!";
    }
    @PostMapping("/ask")
    public String askQuestion(@RequestBody ChatRequest request) {
        return ragService.ask(request.getMessage());
    }
}