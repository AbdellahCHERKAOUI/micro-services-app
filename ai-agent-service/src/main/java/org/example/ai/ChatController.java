package org.example.ai;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;
@RestController
public class ChatController {
    public record ChatRequest(String conversationId, String message) {}
    private final ChatClient chatClient;
    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @PostMapping(value = "/chat", produces = "text/plain")
    public String chat(@RequestBody ChatRequest req) {
        return chatClient.prompt()
            .user(req.message())
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, req.conversationId()))
            .call().content();
    }
    @GetMapping(value = "/chat", produces = "text/plain")
    public String chatGet(@RequestParam String conversationId, @RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call().content();
    }
}
