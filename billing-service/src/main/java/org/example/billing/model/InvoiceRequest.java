package org.example.billing.model;
import java.util.List;
public record InvoiceRequest(Long customerId, List<ItemRequest> items) {}
