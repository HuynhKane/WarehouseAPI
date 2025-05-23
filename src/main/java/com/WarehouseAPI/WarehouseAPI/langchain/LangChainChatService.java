package com.WarehouseAPI.WarehouseAPI.langchain;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LangChainChatService {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    public String askWithContext(String question, String context) {
        String prompt = "Bạn là trợ lý AI về kho hàng. Dưới đây là thông tin:\n"
                + context + "\n\nCâu hỏi: " + question;

        return chatLanguageModel.chat(prompt);
    }
}