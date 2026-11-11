package org.example.customer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class CustomerServiceApplication {
    public static void main(String[] args) { SpringApplication.run(CustomerServiceApplication.class, args); }
    @Bean
    CommandLineRunner start(CustomerRepository repo) {
        return args -> {
            repo.save(new Customer(null, "Hassan", "hassan@gmail.com"));
            repo.save(new Customer(null, "Mohamed", "mohamed@gmail.com"));
            repo.save(new Customer(null, "Imane", "imane@gmail.com"));
        };
    }
    @Bean
    ToolCallbackProvider customerToolCallbacks(CustomerTools tools) {
        return MethodToolCallbackProvider.builder().toolObjects(tools).build();
    }
}
