package com.QuizGen.Master.controller;



import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIController {

    private final ChatClient chatClient;

    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/api/ai/test")
    public String testAI(
            @RequestParam(
                    defaultValue = "Explain Java in one sentence."
            )
            String message) {

        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }
}

