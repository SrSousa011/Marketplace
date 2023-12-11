package com.marketplace.springboot.Service;

import com.marketplace.springboot.DTO.MakerteplaceRecordDto;
import com.marketplace.springboot.Model.MarketplaceModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MarketplaceService {
    MarketplaceModel update(UUID id, MakerteplaceRecordDto makerteplaceRecordDto);
    List<MarketplaceModel> getAllProducts();
    Optional<MarketplaceModel> findById(UUID id);
    MarketplaceModel save(MarketplaceModel marketplaceModel);
    void delete(UUID id);
}
