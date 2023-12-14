package com.marketplace.springboot.Service;

import com.marketplace.springboot.DTO.MarketplaceRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.MarketplaceModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MarketplaceService {
    MarketplaceModel update(UUID id, MarketplaceRecordDto marketplaceRecordDto) throws NotFoundException;

    MarketplaceModel save(MarketplaceModel marketplaceModel);

    List<MarketplaceModel> getAllProducts() throws NotFoundException;

    Optional<MarketplaceModel> getProductById(UUID id) throws NotFoundException;

    void delete(UUID id) throws DeletedException, NotFoundException;
}