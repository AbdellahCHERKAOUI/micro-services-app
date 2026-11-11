package org.example.billing;
import org.example.billing.entities.*;
import org.example.billing.feign.*;
import org.example.billing.model.*;
import org.example.billing.repository.*;
import org.example.billing.web.BillingTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import java.util.*;
@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {
    public static void main(String[] args) { SpringApplication.run(BillingServiceApplication.class, args); }
    @Bean
    CommandLineRunner start(InvoiceRepository invoices, CustomerRestClient customers, ProductRestClient products) {
        return args -> {
            try {
                Customer c = customers.findCustomerById(1L);
                Invoice inv = Invoice.builder().date(new Date()).customerId(c.getId()).build();
                List<InvoiceItem> items = new ArrayList<>();
                for (Product p : products.findAllProducts()) {
                    items.add(InvoiceItem.builder().invoice(inv).productId(p.getId())
                            .quantity(1 + new Random().nextInt(5)).unitPrice(p.getPrice()).build());
                }
                inv.setInvoiceItems(items);
                invoices.save(inv);
            } catch (Exception e) {
                System.out.println("Données de test non créées (customer/inventory pas encore prêts) : " + e.getMessage());
            }
        };
    }
    @Bean
    ToolCallbackProvider billingToolCallbacks(BillingTools tools) {
        return MethodToolCallbackProvider.builder().toolObjects(tools).build();
    }
}
