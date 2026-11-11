package org.example.ai;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AgentConfig {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider tools, ChatMemory memory) {
        return builder
            .defaultSystem("""
                Tu es un assistant de gestion de facturation. Tu disposes d'outils pour gérer
                les clients, les produits et les factures. Utilise-les pour répondre, ne devine
                jamais une donnée. Avant de créer une facture, vérifie que le client et les
                produits existent. Réponds toujours en français, de façon concise.""")
            .defaultToolCallbacks(tools)
            .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
            .build();
    }
}
