package org.example.billing.feign;
import org.example.billing.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@FeignClient(name = "inventory-service")
public interface ProductRestClient {
    @GetMapping("/products/{id}")
    Product findProductById(@PathVariable("id") Long id);
    @GetMapping("/products")
    List<Product> findAllProducts();
}
