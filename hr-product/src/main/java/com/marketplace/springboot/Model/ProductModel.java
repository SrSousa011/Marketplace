package com.marketplace.springboot.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.marketplace.springboot.Exception.Impl.InsufficientStockException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_product")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer stockQuantity;
    private String description;
    private LocalDateTime createdAt;

    public void decreaseStockQuantity(int quantity) {
        if (quantity > 0 && quantity <= stockQuantity) {
            stockQuantity -= quantity;
        } else {
            throw new InsufficientStockException("Insufficient stock for product " + productName);
        }
    }

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
