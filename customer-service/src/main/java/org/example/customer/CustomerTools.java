package org.example.customer;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CustomerTools {
    private final CustomerRepository repo;
    public CustomerTools(CustomerRepository repo) {
        this.repo = repo;
    }
    @Tool(description = "Retourne la liste de tous les clients")
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }
    @Tool(description = "Retourne un client à partir de son identifiant")
    public Customer getCustomerById(@ToolParam(description = "Identifiant du client") Long id) {
        return repo.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Client introuvable : " + id));
    }
    @Tool(description = "Recherche des clients par nom (recherche partielle)")
    public List<Customer> searchCustomersByName(@ToolParam(description = "Partie du nom") String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }
    @Tool(description = "Crée un nouveau client")
    public Customer createCustomer(@ToolParam(description = "Nom") String name, @ToolParam(description = "Email") String email) {
        return repo.save(new Customer(null, name, email));
    }
}
