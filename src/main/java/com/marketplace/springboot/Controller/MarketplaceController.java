package com.marketplace.springboot.Controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.springboot.DTO.MakerteplaceRecordDto;
import com.marketplace.springboot.Model.MarketplaceModel;
import com.marketplace.springboot.Repository.MarketplaceRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class MarketplaceController {
    
    @Autowired
    MarketplaceRepository marketplaceRepository;

    @PostMapping("/products")
    public ResponseEntity <MarketplaceModel> saveMarketplace (@RequestBody @Valid MakerteplaceRecordDto makerteplaceRecordDto) {
        var marketplaceModel = new MarketplaceModel();
        BeanUtils.copyProperties(makerteplaceRecordDto, marketplaceModel);       
		return ResponseEntity.status(HttpStatus.CREATED).body(marketplaceRepository.save(marketplaceModel));
    }
}

