package org.example.billing.web;
import org.example.billing.entities.*;
import org.example.billing.feign.*;
import org.example.billing.model.*;
import org.example.billing.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class InvoiceService {
    private final InvoiceRepository repo;
    private final CustomerRestClient customers;
    private final ProductRestClient products;
    public InvoiceService(InvoiceRepository repo, CustomerRestClient customers, ProductRestClient products) {
        this.repo = repo; this.customers = customers; this.products = products;
    }
    public Invoice enrich(Invoice inv) {
        inv.setCustomer(customers.findCustomerById(inv.getCustomerId()));
        inv.getInvoiceItems().forEach(i -> i.setProduct(products.findProductById(i.getProductId())));
        return inv;
    }
    public Invoice get(Long id) {
        return enrich(repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Facture introuvable : " + id)));
    }
    public List<Invoice> all() {
        return repo.findAll().stream().map(this::enrich).toList();
    }
    public List<Invoice> byCustomer(Long customerId) {
        return repo.findByCustomerId(customerId).stream().map(this::enrich).toList();
    }
    public Invoice create(InvoiceRequest req) {
        customers.findCustomerById(req.customerId());
        Invoice inv = Invoice.builder().date(new Date()).customerId(req.customerId()).build();
        List<InvoiceItem> items = new ArrayList<>();
        for (ItemRequest r : req.items()) {
            Product p = products.findProductById(r.productId());
            items.add(InvoiceItem.builder().invoice(inv).productId(p.getId()).quantity(r.quantity()).unitPrice(p.getPrice()).build());
        }
        inv.setInvoiceItems(items);
        return enrich(repo.save(inv));
    }
}
