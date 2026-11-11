package org.example.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bot Telegram en long polling : lit les messages (getUpdates), les envoie à l'agent AI
 * (POST /chat) et renvoie la réponse. conversationId = id du chat Telegram,
 * donc chaque utilisateur a sa propre mémoire côté agent.
 */
@Component
public class TelegramBot implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    private static final int TELEGRAM_MAX = 4000; // limite Telegram : 4096 caractères

    private final String token;
    private final RestClient telegram;
    private final RestClient agent;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    public TelegramBot(@Value("${telegram.bot.token}") String token,
                       @Value("${agent.url}") String agentUrl) {
        this.token = token;
        this.telegram = RestClient.builder()
                .baseUrl("https://api.telegram.org/bot" + token)
                .requestFactory(factory(45))      // long polling = 30 s côté Telegram
                .build();
        this.agent = RestClient.builder()
                .baseUrl(agentUrl)
                .requestFactory(factory(300))     // l'agent (LLM local) peut être lent
                .build();
    }

    private static JdkClientHttpRequestFactory factory(int readTimeoutSeconds) {
        JdkClientHttpRequestFactory f = new JdkClientHttpRequestFactory(
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build());
        f.setReadTimeout(Duration.ofSeconds(readTimeoutSeconds));
        return f;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (token == null || token.isBlank()) {
            log.error("TELEGRAM_BOT_TOKEN est vide : le bot ne démarre pas. Définir la variable d'environnement.");
            return;
        }
        Thread t = new Thread(this::pollLoop, "telegram-poller");
        t.setDaemon(true);
        t.start();
    }

    private void pollLoop() {
        try { // un webhook actif bloquerait getUpdates (erreur 409)
            telegram.post().uri("/deleteWebhook").retrieve().toBodilessEntity();
        } catch (Exception e) {
            log.warn("deleteWebhook : {}", safe(e));
        }
        log.info("Bot Telegram démarré, en attente de messages...");
        long offset = 0;
        while (true) {
            try {
                JsonNode res = telegram.get()
                        .uri("/getUpdates?timeout=30&offset={o}", offset)
                        .retrieve().body(JsonNode.class);
                if (res == null) continue;
                for (JsonNode update : res.path("result")) {
                    offset = update.path("update_id").asLong() + 1;
                    JsonNode msg = update.path("message");
                    if (msg.hasNonNull("text")) {
                        long chatId = msg.path("chat").path("id").asLong();
                        String text = msg.path("text").asText();
                        pool.submit(() -> handle(chatId, text));
                    }
                }
            } catch (Exception e) {
                log.warn("Erreur de polling : {}", safe(e));
                sleep(3000);
            }
        }
    }

    private void handle(long chatId, String text) {
        try {
            if (text.startsWith("/start")) {
                send(chatId, "Bonjour ! Je suis l'assistant de facturation. "
                        + "Je peux gérer les clients, les produits et les factures. "
                        + "Exemple : « Montre-moi la liste des clients ».");
                return;
            }
            telegram.post().uri("/sendChatAction")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("chat_id", chatId, "action", "typing"))
                    .retrieve().toBodilessEntity();

            String answer = agent.post().uri("/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("conversationId", String.valueOf(chatId), "message", text))
                    .retrieve().body(String.class);

            send(chatId, (answer == null || answer.isBlank()) ? "(réponse vide)" : answer);
        } catch (Exception e) {
            log.error("Erreur pour le chat {} : {}", chatId, safe(e));
            try {
                send(chatId, "Désolé, une erreur est survenue en contactant l'agent. Réessayez dans un instant.");
            } catch (Exception ignored) { }
        }
    }

    private void send(long chatId, String text) {
        for (int i = 0; i < text.length(); i += TELEGRAM_MAX) {
            String part = text.substring(i, Math.min(text.length(), i + TELEGRAM_MAX));
            telegram.post().uri("/sendMessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("chat_id", chatId, "text", part))
                    .retrieve().toBodilessEntity();
        }
    }

    /** Les messages d'erreur HTTP contiennent l'URL, donc le token : on le masque dans les logs. */
    private String safe(Exception e) {
        String m = String.valueOf(e.getMessage());
        return (token == null || token.isBlank()) ? m : m.replace(token, "***");
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }
}
