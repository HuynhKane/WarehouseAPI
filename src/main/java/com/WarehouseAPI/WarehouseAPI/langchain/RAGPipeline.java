package com.WarehouseAPI.WarehouseAPI.langchain;

import com.WarehouseAPI.WarehouseAPI.embeddings.EmbeddingService;
import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RAGPipeline {

    @Autowired
    ProductRepository productRepo;
    @Autowired
    EmbeddingService embeddingService;
    @Autowired
    FaissService faissService;
    @Autowired
    LangChainChatService langChainChatService;

    public void indexAll() {
        List<Product> products = productRepo.findAll();
        List<List<Double>> embeddings = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        for (Product p : products) {
            embeddings.add(embeddingService.getEmbedding(p.toString()));
            ids.add(p.get_id());
        }

        faissService.indexVectors(embeddings, ids);
    }

    public String ask(String question) {
        List<Double> queryVec = embeddingService.getEmbedding(question);
        List<String> topIds = faissService.search(queryVec, 3);

        StringBuilder context = new StringBuilder();
        for (String id : topIds) {
            productRepo.findById(id).ifPresent(p -> context.append(p).append("\n"));
        }

        return langChainChatService.askWithContext(question, context.toString());
    }
}

