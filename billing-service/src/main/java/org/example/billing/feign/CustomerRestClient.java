package org.example.billing.feign;
import org.example.billing.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "customer-service")
public interface CustomerRestClient {
    @GetMapping("/customers/{id}")
    Customer findCustomerById(@PathVariable("id") Long id);
}
