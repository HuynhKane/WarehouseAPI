package com.WarehouseAPI.WarehouseAPI.langchain;

import com.WarehouseAPI.WarehouseAPI.embeddings.EmbeddingService;
import com.WarehouseAPI.WarehouseAPI.model.*;
import com.WarehouseAPI.WarehouseAPI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;

@Service
public class RAGPipeline {

    @Autowired
    ProductRepository productRepo;
    @Autowired
    ImportPackageRepos importRepo;
    @Autowired
    ExportPackageRepos exportRepo;
    @Autowired
    SupplierRepository supplierRepo;
    @Autowired
    CustomerRepository customerRepo;
    @Autowired
    EmbeddingService embeddingService;
    @Autowired
    FaissService faissService;
    @Autowired
    LangChainChatService langChainChatService;


    private static final String EMBEDDING_FILE = "data/embeddings.ser";



    @PostConstruct
    public void init() {
        try {
            loadEmbeddingData(EMBEDDING_FILE);
            System.out.println("✅ FAISS index loaded from file.");
        } catch (Exception e) {
            System.out.println("⚠️ Embedding data not found. Indexing from scratch...");
            indexAll();
        }
    }

    public void indexAll() {
        List<List<Double>> embeddings = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        Map<String, List<?>> dataSources = Map.of(
                "PRODUCT", productRepo.findAll(),
                "IMPORT", importRepo.findAll(),
                "EXPORT", exportRepo.findAll(),
                "SUPPLIER", supplierRepo.findAll(),
                "CUSTOMER", customerRepo.findAll()
        );

        dataSources.forEach((prefix, list) -> list.forEach(item -> {
            embeddings.add(embeddingService.getEmbedding(item.toString()));
            String id = getIdFromEntity(item);
            ids.add(prefix + "_" + id);
        }));

        faissService.indexVectors(embeddings, ids);

        try {
            saveEmbeddingData(embeddings, ids, EMBEDDING_FILE);
            System.out.println("✅ Embeddings saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getIdFromEntity(Object entity) {
        try {
            Method getIdMethod = Arrays.stream(entity.getClass().getMethods())
                    .filter(m -> m.getName().startsWith("get") && m.getName().toLowerCase().contains("id"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No ID getter found in " + entity.getClass()));

            return String.valueOf(getIdMethod.invoke(entity));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract ID from entity: " + entity, e);
        }
    }
    public String ask(String question) {
        List<Double> queryVec = embeddingService.getEmbedding(question);
        List<String> topIds = faissService.search(queryVec, 3);

        StringBuilder context = new StringBuilder();
        for (String id : topIds) {
            if (id.startsWith("PRODUCT_")) {
                String pid = id.replace("PRODUCT_", "");
                productRepo.findById(pid).ifPresent(p -> context.append(p).append("\n"));
            }
            // Add more logic for other types if needed
            else if (id.startsWith("IMPORT_")) {
                String pid = id.replace("IMPORT_", "");
                importRepo.findById(pid).ifPresent(p -> context.append(p).append("\n"));
            }
            else if (id.startsWith("EXPORT_")) {
                String pid = id.replace("EXPORT_", "");
                exportRepo.findById(pid).ifPresent(p -> context.append(p).append("\n"));
            }
            else if (id.startsWith("SUPPLIER_")) {
                String pid = id.replace("SUPPLIER_", "");
                supplierRepo.findById(pid).ifPresent(p -> context.append(p).append("\n"));

            }
            else if (id.startsWith("CUSTOMER_")) {
                String pid = id.replace("CUSTOMER_", "");
                customerRepo.findById(pid).ifPresent(p -> context.append(p).append("\n"));
            }

        }

        return langChainChatService.askWithContext(question, context.toString());
    }

    public void saveEmbeddingData(List<List<Double>> vectors, List<String> ids, String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(vectors);
            oos.writeObject(ids);
        }
    }

    public void loadEmbeddingData(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            List<List<Double>> vectors = (List<List<Double>>) ois.readObject();
            List<String> ids = (List<String>) ois.readObject();
            faissService.indexVectors(vectors, ids);
        }
    }

}

