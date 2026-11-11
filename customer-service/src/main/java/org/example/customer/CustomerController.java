package org.example.customer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/customers")
@RefreshScope
public class CustomerController {
    private final CustomerRepository repo;
    @Value("${global.params.p1:default1}")
    private String p1;
    @Value("${global.params.p2:default2}")
    private String p2;
    public CustomerController(CustomerRepository repo) {
        this.repo = repo; }
    @GetMapping
    public List<Customer> all() {
        return repo.findAll(); }
    @GetMapping("/{id}")
    public Customer one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public Customer create(@RequestBody Customer c) {
        c.setId(null);
        return repo.save(c);
    }
    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer c) {
        c.setId(id);
        return repo.save(c);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        repo.deleteById(id);
    }
    @GetMapping("/config")
    public Map<String,String> config() {
        return Map.of("p1", p1, "p2", p2);
    }
}
