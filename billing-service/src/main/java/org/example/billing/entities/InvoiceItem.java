package org.example.billing.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.billing.model.Product;
@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InvoiceItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    @Transient
    private Product product;
    private int quantity;
    private double unitPrice;
    @ManyToOne @JsonIgnore
    private Invoice invoice;
}
