package com.marketplace.springboot.Service.Impl;

import com.marketplace.springboot.DTO.ProductRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.DuplicatedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.ProductModel;
import com.marketplace.springboot.Repository.ProductRepository;
import com.marketplace.springboot.Service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductModel update(UUID id, @Valid ProductRecordDto productRecordDto) throws NotFoundException {
        Optional<ProductModel> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            ProductModel existingProductModel = existingProduct.get();
            if (isAlreadyExisting(existingProductModel.getProductName(), productRecordDto.getProductName())) {
                throw new DuplicatedException("Product with name '" + productRecordDto.getProductName() + "' already exists.");
            }
            existingProductModel.setProductName(productRecordDto.getProductName());
            existingProductModel.setProductPrice(productRecordDto.getProductPrice());
            existingProductModel.setDescription(productRecordDto.getDescription());
            existingProductModel.setQuantityAvailable(productRecordDto.getQuantityAvailable());

            return productRepository.save(existingProductModel);
        } else {
            throw new NotFoundException("Product with ID " + id);
        }
    }

    private boolean isAlreadyExisting(String existingName, String newName) {
        return !existingName.equals(newName) && productRepository.existsByProductName(newName);
    }

    @Transactional
    public ProductModel save(ProductModel productModel) {
        if (isAlreadyExisting(productModel)) {
            throw new DuplicatedException("Product with name '" + productModel.getProductName());
        }
        return productRepository.save(productModel);
    }

    private boolean isAlreadyExisting(ProductModel productModel) {
        return productRepository.existsByProductName(productModel.getProductName());
    }

    @Transactional
    public List<ProductModel> getAllProducts() {
        List<ProductModel> productsList = productRepository.findAll();
        if (productsList.isEmpty()) {
            throw new NotFoundException("No products has been found");
        }
        return productsList;
    }

    @Transactional
    public Optional<ProductModel> getProductById(UUID id) throws NotFoundException {
        Optional<ProductModel> productO = productRepository.findById(id);

        if (productO.isEmpty()) {
            throw new NotFoundException("Product with ID " + id);
        }

        return productRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id) throws DeletedException, NotFoundException {
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()) {
            throw new NotFoundException("Product with ID " + id);
        }

        try {
            productRepository.delete(productO.get());
            throw new DeletedException("Product with ID " + id + " has been deleted.");
        } catch (Exception e) {
            throw new DeletedException("Failed to delete product with ID " + id + ".");
        }
    }

}