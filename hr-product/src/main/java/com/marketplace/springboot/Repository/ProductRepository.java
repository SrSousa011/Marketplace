package com.marketplace.springboot.Repository;

import java.util.UUID;

import com.marketplace.springboot.Model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    boolean existsByProductName(String productName);
}
