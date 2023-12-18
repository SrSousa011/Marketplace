package com.marketplace.springboot.Service;

import com.marketplace.springboot.DTO.ProductRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.ProductModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    ProductModel update(UUID id, ProductRecordDto marketplaceRecordDto) throws NotFoundException;

    ProductModel save(ProductModel marketplaceModel);

    List<ProductModel> getAllProducts() throws NotFoundException;

    Optional<ProductModel> getProductById(UUID id) throws NotFoundException;

    void delete(UUID id) throws DeletedException, NotFoundException;
}