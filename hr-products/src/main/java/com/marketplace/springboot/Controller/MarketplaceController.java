package com.marketplace.springboot.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Service.MarketplaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.marketplace.springboot.DTO.MarketplaceRecordDto;
import com.marketplace.springboot.Model.MarketplaceModel;
import com.marketplace.springboot.Repository.MarketplaceRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
@Tag(name = "Marketplace API REST", description = "Endpoints for managing marketplace products.")
public class MarketplaceController {
    private static final Logger logger = LoggerFactory.getLogger(MarketplaceController.class);
    private final MarketplaceRepository marketplaceRepository;
    private final MarketplaceService marketplaceService;

    @Autowired
    public MarketplaceController(MarketplaceRepository marketplaceRepository, MarketplaceService marketplaceService) {
        this.marketplaceRepository = marketplaceRepository;
        this.marketplaceService = marketplaceService;
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid MarketplaceRecordDto marketplaceRecordDto) {
        try {
            MarketplaceModel updatedProduct = marketplaceService.update(id, marketplaceRecordDto);
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
            List<MarketplaceModel> productsList = marketplaceService.getAllProducts();
            List<EntityModel<MarketplaceModel>> productsWithLinks = new ArrayList<>();

            for (MarketplaceModel product : productsList) {
                UUID productId = product.getProductId();
                EntityModel<MarketplaceModel> productWithLink = EntityModel.of(product);
                productWithLink.add(linkTo(methodOn(MarketplaceController.class).getOneProduct(productId)).withSelfRel());
                productsWithLinks.add(productWithLink);
            }

            CollectionModel<EntityModel<MarketplaceModel>> collectionModel = CollectionModel.of(productsWithLinks);
            collectionModel.add(linkTo(methodOn(MarketplaceController.class).getAllProducts()).withSelfRel());

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
            Optional<MarketplaceModel> product = marketplaceService.getProductById(id);

            if (product.isPresent()) {
                logger.info("Retrieved details of product with ID {}", id);
                EntityModel<Optional<MarketplaceModel>> resource = EntityModel.of(product);
                resource.add(linkTo(methodOn(MarketplaceController.class).getAllProducts()).withRel("Products List"));
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

    @PostMapping("/products")
    @Operation(summary = "Save a new product with the provided details.")
    public ResponseEntity<MarketplaceModel> saveProduct(@RequestBody @Valid MarketplaceRecordDto marketplaceRecordDto) {
        try {
            var marketplaceModel = new MarketplaceModel();
            marketplaceModel.setProductId(marketplaceRecordDto.getProductId());
            marketplaceModel.setName(marketplaceRecordDto.getName());
            marketplaceModel.setPrice(marketplaceRecordDto.getPrice());
            marketplaceModel.setQuantityAvailable(marketplaceRecordDto.getQuantityAvailable());
            marketplaceModel.setEmail(marketplaceRecordDto.getEmail());
            marketplaceModel.setPassword(marketplaceRecordDto.getPassword());
            marketplaceModel.setCreatedAt(marketplaceRecordDto.getCreatedAt());

            MarketplaceModel savedProduct = marketplaceService.save(marketplaceModel);
            logger.debug("Product saved successfully: {}", savedProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            logger.error("Error saving product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        try {
            marketplaceService.delete(id);
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

    @ExceptionHandler({NotFoundException.class, DeletedException.class})
    public ResponseEntity<Object> handleExceptions(Exception e) {
        logger.error("Exception occurred", e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}