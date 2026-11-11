package org.example.billing.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.example.billing.model.Customer;
import java.util.Date;
import java.util.List;
@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private Long customerId;
    @Transient
    private Customer customer;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceItem> invoiceItems;
    @JsonProperty("total")
    public double total() {
        return invoiceItems == null ? 0 : invoiceItems.stream().mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum();
    }
}
