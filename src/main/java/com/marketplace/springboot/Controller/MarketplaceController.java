package com.marketplace.springboot.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.marketplace.springboot.Service.MarketplaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.marketplace.springboot.DTO.MakerteplaceRecordDto;
import com.marketplace.springboot.Model.MarketplaceModel;
import com.marketplace.springboot.Repository.MarketplaceRepository;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
@Tag(name = "Marketplace API REST", description = "Endpoints for managing marketplace products.")

public class MarketplaceController {

    @Autowired
    MarketplaceRepository marketplaceRepository;

    @Autowired
    private MarketplaceService marketplaceService;

    @GetMapping("/products")
    @Operation(summary = "Retrieve a list of all products")
    public ResponseEntity<List<MarketplaceModel>> getAllProducts() {
        List<MarketplaceModel> productsList = marketplaceRepository.findAll();
        if (!productsList.isEmpty()) {
            for (MarketplaceModel product : productsList) {
                UUID id = product.getProductId();
                product.add(linkTo(methodOn(MarketplaceController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    @GetMapping("/product/{id}")
    @Operation(summary = "Retrieve details of a specific product by its ID.")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<MarketplaceModel> productO = marketplaceRepository.findById(id);
        if (productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + " not found.");
        }
        productO.get().add(linkTo(methodOn(MarketplaceController.class).getAllProducts()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }

    @PostMapping("/products")
    @Operation(summary = "Save a new product with the provided details.")
    public ResponseEntity<MarketplaceModel> saveProduct(
            @RequestBody @Valid MakerteplaceRecordDto makerteplaceRecordDto) {
        var marketplaceModel = new MarketplaceModel();
        BeanUtils.copyProperties(makerteplaceRecordDto, marketplaceModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(marketplaceRepository.save(marketplaceModel));
    }

    @DeleteMapping("/product/{id}")
    @Operation(summary = "Delete a product with the specified ID.")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<MarketplaceModel> productO = marketplaceRepository.findById(id);
        if (productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + " not found.");
        }
        marketplaceRepository.delete(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product with ID " + id + " successfully deleted.");
    }

    @PutMapping("/products/{id}")
    @Operation(summary = "Update details of a product with the specified ID.")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid MakerteplaceRecordDto makerteplaceRecordDto) {
        Optional<MarketplaceModel> productO = marketplaceRepository.findById(id);
        if (productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + " not found.");
        }
        var marketplaceModel = productO.get();
        BeanUtils.copyProperties(makerteplaceRecordDto, marketplaceModel);
        return ResponseEntity.status(HttpStatus.OK).body(marketplaceRepository.save(marketplaceModel));
    }
}

