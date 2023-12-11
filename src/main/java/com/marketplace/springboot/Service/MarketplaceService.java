package com.marketplace.springboot.Service;

import com.marketplace.springboot.DTO.MakerteplaceRecordDto;
import com.marketplace.springboot.Model.MarketplaceModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MarketplaceService {
    List<MarketplaceModel> getAllProducts();
    Optional<MarketplaceModel> findById(UUID id);
    MarketplaceModel save(MarketplaceModel marketplaceModel);
    MarketplaceModel update(UUID id, MakerteplaceRecordDto makerteplaceRecordDto);
    void delete(UUID id);
}
