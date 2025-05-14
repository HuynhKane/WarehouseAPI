package com.WarehouseAPI.WarehouseAPI.langchain;



import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LangChainConfig {
    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Bean
    public OpenAiChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName("gpt-3.5-turbo-0125") // Hoặc gpt-3.5-turbo
                .temperature(0.3)
                .build();
    }
}
