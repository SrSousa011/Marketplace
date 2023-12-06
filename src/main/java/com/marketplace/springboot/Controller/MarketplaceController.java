package com.marketplace.springboot.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.marketplace.springboot.DTO.MakerteplaceRecordDto;
import com.marketplace.springboot.Model.MarketplaceModel;
import com.marketplace.springboot.Repository.MarketplaceRepository;

import jakarta.validation.Valid;


@RestController
public class MarketplaceController {
    
    @Autowired
    MarketplaceRepository marketplaceRepository;

    	@GetMapping("/products")
	public ResponseEntity<List<MarketplaceModel>> getAllProducts(){
		return ResponseEntity.status(HttpStatus.OK).body(marketplaceRepository.findAll());
		}


    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value="id") UUID id){
        Optional<MarketplaceModel> productO = marketplaceRepository.findById(id);
        if(productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + "not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }

    @PostMapping("/products")
    public ResponseEntity <MarketplaceModel> saveProduct (@RequestBody @Valid MakerteplaceRecordDto makerteplaceRecordDto) {
        var marketplaceModel = new MarketplaceModel();
        BeanUtils.copyProperties(makerteplaceRecordDto, marketplaceModel);       
		return ResponseEntity.status(HttpStatus.CREATED).body(marketplaceRepository.save(marketplaceModel));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value="id") UUID id,
                                                @RequestBody @Valid MakerteplaceRecordDto makerteplaceRecordDto) {
        Optional<MarketplaceModel> productO = marketplaceRepository.findById(id);
        if(productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var marketplaceModel = productO.get();
        BeanUtils.copyProperties(makerteplaceRecordDto, marketplaceModel);
        return ResponseEntity.status(HttpStatus.OK).body(marketplaceRepository.save(marketplaceModel));
    }

}

}
