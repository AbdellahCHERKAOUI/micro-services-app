package org.example.billing.web;
import org.example.billing.entities.Invoice;
import org.example.billing.model.*;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class BillingTools {
    private final InvoiceService service;
    public BillingTools(InvoiceService service) { this.service = service; }
    @Tool(description = "Retourne la liste de toutes les factures (avec client, produits et total)")
    public List<Invoice> getAllInvoices() { return service.all(); }
    @Tool(description = "Retourne une facture complète à partir de son identifiant")
    public Invoice getInvoiceById(@ToolParam(description = "Identifiant de la facture") Long id) { return service.get(id); }
    @Tool(description = "Retourne les factures d'un client")
    public List<Invoice> getInvoicesByCustomer(@ToolParam(description = "Identifiant du client") Long customerId) { return service.byCustomer(customerId); }
    @Tool(description = "Crée une facture pour un client avec une liste de produits (productId, quantity)")
    public Invoice createInvoice(@ToolParam(description = "Identifiant du client") Long customerId,
                                 @ToolParam(description = "Lignes de la facture : productId et quantity") List<ItemRequest> items) {
        return service.create(new InvoiceRequest(customerId, items));
    }
}
