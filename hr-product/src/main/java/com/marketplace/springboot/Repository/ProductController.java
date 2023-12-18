package com.marketplace.springboot.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marketplace.springboot.Model.ProductModel;

@Repository
public interface ProductController extends JpaRepository<ProductModel, UUID> {
    boolean existsByName(String name);
}
