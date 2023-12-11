package com.marketplace.springboot.Service;

import com.marketplace.springboot.DTO.MakerteplaceRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedUserException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.MarketplaceModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MarketplaceService {
    MarketplaceModel update(UUID id, MakerteplaceRecordDto makerteplaceRecordDto) throws NotFoundException;

    MarketplaceModel save(MarketplaceModel marketplaceModel);

    List<MarketplaceModel> getAllProducts() throws NotFoundException;

    Optional<MarketplaceModel> findById(UUID id) throws NotFoundException;

    void delete(UUID id) throws DeletedUserException, NotFoundException;
}
