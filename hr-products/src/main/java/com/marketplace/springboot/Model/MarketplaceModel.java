package com.marketplace.springboot.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
@Table(name = "tbl_marketplace")
public class MarketplaceModel extends RepresentationModel<MarketplaceModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;
    private String name;
    private BigDecimal price;
    private Integer quantityAvailable;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
