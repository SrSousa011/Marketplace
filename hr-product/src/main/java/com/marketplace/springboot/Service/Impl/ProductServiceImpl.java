package com.marketplace.springboot.Service.Impl;

import com.marketplace.springboot.DTO.ProductRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.DuplicatedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.ProductModel;
import com.marketplace.springboot.Repository.ProductController;
import com.marketplace.springboot.Service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductController productController;

    public ProductServiceImpl(ProductController productController) {
        this.productController = productController;
    }

    @Transactional
    public ProductModel update(UUID id, @Valid ProductRecordDto productRecordDto) throws NotFoundException {
        Optional<ProductModel> existingProduct = productController.findById(id);

        if (existingProduct.isPresent()) {
            ProductModel existingProductModel = existingProduct.get();
            if (isAlreadyExisting(existingProductModel.getProductName(), productRecordDto.getName())) {
                throw new DuplicatedException("Product with name '" + productRecordDto.getName() + "' already exists.");
            }
            existingProductModel.setProductName(productRecordDto.getName());
            existingProductModel.setProductPrice(productRecordDto.getPrice());
            existingProductModel.setDescription(productRecordDto.getDescription());
            existingProductModel.setQuantityAvailable(productRecordDto.getQuantityAvailable());

            return productController.save(existingProductModel);
        } else {
            throw new NotFoundException("Product with ID " + id);
        }
    }

    private boolean isAlreadyExisting(String existingName, String newName) {
        return !existingName.equals(newName) && productController.existsByName(newName);
    }

    @Transactional
    public ProductModel save(ProductModel productModel) {
        if (isAlreadyExisting(productModel)) {
            throw new DuplicatedException("Product with name '" + productModel.getProductName());
        }
        return productController.save(productModel);
    }

    private boolean isAlreadyExisting(ProductModel productModel) {
        return productController.existsByName(productModel.getProductName());
    }

    @Transactional
    public List<ProductModel> getAllProducts() {
        List<ProductModel> productsList = productController.findAll();
        if (productsList.isEmpty()) {
            throw new NotFoundException("No products has been found");
        }
        return productsList;
    }

    @Transactional
    public Optional<ProductModel> getProductById(UUID id) throws NotFoundException {
        Optional<ProductModel> productO = productController.findById(id);

        if (productO.isEmpty()) {
            throw new NotFoundException("Product with ID " + id);
        }

        return productController.findById(id);
    }

    @Transactional
    public void delete(UUID id) throws DeletedException, NotFoundException {
        Optional<ProductModel> productO = productController.findById(id);
        if (productO.isEmpty()) {
            throw new NotFoundException("Product with ID " + id);
        }

        try {
            productController.delete(productO.get());
            throw new DeletedException("User with ID " + id + " has been deleted.");
        } catch (Exception e) {
            throw new DeletedException("Failed to delete user with ID " + id + ".");
        }
    }

}