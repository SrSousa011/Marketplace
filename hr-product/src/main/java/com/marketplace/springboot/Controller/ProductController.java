package com.marketplace.springboot.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.marketplace.springboot.DTO.ProductRecordDto;
import com.marketplace.springboot.Model.ProductModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(name = "Marketplace API REST", description = "Endpoints for managing marketplace products.")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        try {
            ProductModel updatedProduct = productService.update(id, productRecordDto);
            logger.info("Product with ID {} successfully updated.", id);
            return ResponseEntity.ok(updatedProduct);
        } catch (NotFoundException e) {
            logger.error("Product update failed. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductModel> productsList = productService.getAllProducts();
            List<EntityModel<ProductModel>> productsWithLinks = new ArrayList<>();

            for (ProductModel product : productsList) {
                UUID productId = product.getProductId();
                EntityModel<ProductModel> productWithLink = EntityModel.of(product);
                productWithLink.add(linkTo(methodOn(ProductController.class).getOneProduct(productId)).withSelfRel());
                productsWithLinks.add(productWithLink);
            }

            CollectionModel<EntityModel<ProductModel>> collectionModel = CollectionModel.of(productsWithLinks);
            collectionModel.add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());

            logger.info("Retrieved a list of all products.");
            return ResponseEntity.status(HttpStatus.OK).body(collectionModel);

        } catch (NotFoundException e) {
            logger.error("Error retrieving products. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        try {
            Optional<ProductModel> product = productService.getProductById(id);

            if (product.isPresent()) {
                logger.info("Retrieved details of product with ID {}", id);
                EntityModel<Optional<ProductModel>> resource = EntityModel.of(product);
                resource.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
                return ResponseEntity.ok(resource);
            } else {
                logger.warn("Product not found for ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found for ID: " + id);
            }
        } catch (NotFoundException e) {
            logger.error("Error retrieving product details. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/product")
    @Operation(summary = "Save a new product with the provided details.")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        try {
            var marketplaceModel = getMarketplaceModel(productRecordDto);

            ProductModel savedProduct = productService.save(marketplaceModel);
            logger.debug("Product saved successfully: {}", savedProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            logger.error("Error saving product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private static ProductModel getMarketplaceModel(ProductRecordDto productRecordDto) {
        var marketplaceModel = new ProductModel();
        marketplaceModel.setProductId(productRecordDto.getProductId());
        marketplaceModel.setProductName(productRecordDto.getProductName());
        marketplaceModel.setProductPrice(productRecordDto.getProductPrice());
        marketplaceModel.setQuantityAvailable(productRecordDto.getQuantityAvailable());
        marketplaceModel.setDescription(productRecordDto.getDescription());
        marketplaceModel.setCreatedAt(productRecordDto.getCreatedAt());
        return marketplaceModel;
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        try {
            productService.delete(id);
            logger.info("Product with ID {} successfully deleted.", id);
            return ResponseEntity.status(HttpStatus.OK).body("Product with ID " + id + " successfully deleted.");
        } catch (NotFoundException e) {
            logger.error("Product deletion failed. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DeletedException e) {
            logger.error("Product deletion failed. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
