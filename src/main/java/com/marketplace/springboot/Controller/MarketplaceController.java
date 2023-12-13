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

    @Autowired
    MarketplaceRepository marketplaceRepository;

    @Autowired
    private MarketplaceService marketplaceService;

    @PutMapping("/product/{id}")
    @Operation(summary = "Update details of a product with the specified ID.")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid MarketplaceRecordDto marketplaceRecordDto) {
        try {
            MarketplaceModel updatedProduct = marketplaceService.update(id, marketplaceRecordDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/products")
    @Operation(summary = "Retrieve a list of all products")
    public ResponseEntity<CollectionModel<EntityModel<MarketplaceModel>>> getAllProducts() {
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

            return ResponseEntity.status(HttpStatus.OK).body(collectionModel);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/product/{id}")
    @Operation(summary = "Retrieve details of a specific product by its ID.")
    public ResponseEntity<EntityModel<Optional<MarketplaceModel>>> getOneProduct(@PathVariable(value = "id") UUID id) {
        try {
            Optional<MarketplaceModel> product = marketplaceService.getProductById(id);
            EntityModel<Optional<MarketplaceModel>> resource = EntityModel.of(product);
            resource.add(WebMvcLinkBuilder.linkTo(methodOn(MarketplaceController.class).getAllProducts()).withRel("Products List"));
            return ResponseEntity.ok(resource);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EntityModel.of(Optional.empty()));
        }
    }

    @PostMapping("/products")
    @Operation(summary = "Save a new product with the provided details.")
    public ResponseEntity<MarketplaceModel> saveProduct(@RequestBody @Valid MarketplaceRecordDto marketplaceRecordDto) {
        var marketplaceModel = new MarketplaceModel();
        BeanUtils.copyProperties(marketplaceRecordDto, marketplaceModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(marketplaceService.save(marketplaceModel));
    }

    @DeleteMapping("/product/{id}")
    @Operation(summary = "Delete a product with the specified ID.")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        try {
            marketplaceService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product with ID " + id + " successfully deleted.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DeletedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}