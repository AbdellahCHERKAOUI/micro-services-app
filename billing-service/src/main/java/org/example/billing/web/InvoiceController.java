package org.example.billing.web;
import org.example.billing.entities.Invoice;
import org.example.billing.model.InvoiceRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService service;
    public InvoiceController(InvoiceService service) {
        this.service = service;
    }
    @GetMapping
    public List<Invoice> all() {
        return service.all();
    }
    @GetMapping("/{id}")
    public Invoice one(@PathVariable Long id) {
        return service.get(id);
    }
    @GetMapping("/customer/{customerId}")
    public List<Invoice> byCustomer(@PathVariable Long customerId) {
        return service.byCustomer(customerId);
    }
    @PostMapping
    public Invoice create(@RequestBody InvoiceRequest req) {
        return service.create(req);
    }
}
