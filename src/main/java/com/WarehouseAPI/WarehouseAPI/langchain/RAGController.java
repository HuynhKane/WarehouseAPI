package com.WarehouseAPI.WarehouseAPI.langchain;

import com.WarehouseAPI.WarehouseAPI.dto.ChatRequest;
import com.WarehouseAPI.WarehouseAPI.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ChatResponse> askQuestion(@RequestBody ChatRequest request) {
        String answer = ragService.ask(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(answer));
    }
}


