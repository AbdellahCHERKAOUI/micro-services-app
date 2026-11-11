package org.example.inventory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProductTools {
    private final ProductRepository repo;
    public ProductTools(ProductRepository repo) {
        this.repo = repo;
    }
    @Tool(description = "Retourne la liste de tous les produits avec prix et stock")
    public List<Product> getAllProducts() {
        return repo.findAll();
    }
    @Tool(description = "Retourne un produit à partir de son identifiant")
    public Product getProductById(@ToolParam(description = "Identifiant du produit") Long id) {
        return repo.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Produit introuvable : " + id));
    }
    @Tool(description = "Recherche des produits par nom (recherche partielle)")
    public List<Product> searchProductsByName(@ToolParam(description = "Partie du nom") String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }
    @Tool(description = "Crée un nouveau produit")
    public Product createProduct(@ToolParam(description = "Nom") String name,
                                 @ToolParam(description = "Prix unitaire") double price,
                                 @ToolParam(description = "Quantité en stock") int quantity) {
        return repo.save(new Product(null, name, price, quantity));
    }
}
