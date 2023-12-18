package com.marketplace.springboot.Model;


import com.marketplace.springboot.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_order_product")
public class OrderModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderEntityId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;

    private BigDecimal totalPrice;

    // Uncomment the following lines if you have relationships with other entities
    //@ManyToOne
    //@JoinColumn(name = "user_id")
    //private User user;

    //@OneToMany(mappedBy = "order")
    //private List<OrderItem> orderItems;

    // Constructor with relevant parameters
    public OrderModel(OrderStatus status, LocalDateTime orderProductDate, BigDecimal totalPrice) {
        this.status = status;
        this.orderDate = orderProductDate;
        this.totalPrice = totalPrice;
    }
}
