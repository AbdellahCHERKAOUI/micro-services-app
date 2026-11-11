package org.example.inventory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) { SpringApplication.run(InventoryServiceApplication.class, args); }
    @Bean
    CommandLineRunner start(ProductRepository repo) {
        return args -> {
            repo.save(new Product(null, "Ordinateur", 8500, 20));
            repo.save(new Product(null, "Imprimante", 1200, 15));
            repo.save(new Product(null, "Smartphone", 4300, 40));
        };
    }
    @Bean
    ToolCallbackProvider inventoryToolCallbacks(ProductTools tools) {
        return MethodToolCallbackProvider.builder().toolObjects(tools).build();
    }
}
