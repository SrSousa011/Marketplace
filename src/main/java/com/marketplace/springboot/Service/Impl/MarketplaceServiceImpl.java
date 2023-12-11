package com.marketplace.springboot.Service.Impl;

import com.marketplace.springboot.DTO.MakerteplaceRecordDto;
import com.marketplace.springboot.Model.MarketplaceModel;
import com.marketplace.springboot.Repository.MarketplaceRepository;
import com.marketplace.springboot.Service.MarketplaceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MarketplaceServiceImpl implements MarketplaceService {

    private final MarketplaceRepository marketplaceRepository;

    public MarketplaceServiceImpl(MarketplaceRepository marketplaceRepository) {
        this.marketplaceRepository = marketplaceRepository;
    }

    @Transactional
    public MarketplaceModel save(MarketplaceModel marketplaceModel) {
        return marketplaceRepository.save(marketplaceModel);
    }

    @Transactional
    public List<MarketplaceModel> getAllProducts() {
        return marketplaceRepository.findAll();
    }

    @Transactional
    public Optional<MarketplaceModel> findById(UUID id) {
        return marketplaceRepository.findById(id);
    }

    @Transactional
    public MarketplaceModel update(UUID id, MakerteplaceRecordDto makerteplaceRecordDto) {
        Optional<MarketplaceModel> existingProduct = marketplaceRepository.findById(id);

        if (existingProduct.isPresent()) {
            MarketplaceModel updatedProduct = existingProduct.get();
            updatedProduct.setName(makerteplaceRecordDto.name());
            updatedProduct.setPrice(makerteplaceRecordDto.price());

            return marketplaceRepository.save(updatedProduct);
        } else {
            return null;
        }
    }

    @Transactional
    public void delete(UUID id) {
        marketplaceRepository.deleteById(id);
    }
}
