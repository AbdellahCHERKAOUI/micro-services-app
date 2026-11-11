package org.example.inventory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository repo;
    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }
    @GetMapping
    public List<Product> all() {
        return repo.findAll();
    }
    @GetMapping("/{id}")
    public Product one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public Product create(@RequestBody Product p) {
        p.setId(null);
        return repo.save(p);
    }
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product p) {
        p.setId(id);
        return repo.save(p);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
