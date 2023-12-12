package com.marketplace.springboot.Service.Impl;

import com.marketplace.springboot.DTO.MarketplaceRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.DuplicatedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.MarketplaceModel;
import com.marketplace.springboot.Repository.MarketplaceRepository;
import com.marketplace.springboot.Service.MarketplaceService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    public MarketplaceModel update(UUID id, @Valid MarketplaceRecordDto marketplaceRecordDto) throws NotFoundException {
        Optional<MarketplaceModel> existingProduct = marketplaceRepository.findById(id);

        if (existingProduct.isPresent()) {
            MarketplaceModel existingProductModel = existingProduct.get();
            if (isAlreadyExisting(existingProductModel.getName(), marketplaceRecordDto.name())) {
                throw new DuplicatedException("Product with name '" + marketplaceRecordDto.name() + "' already exists.");
            }
            existingProductModel.setName(marketplaceRecordDto.name());
            existingProductModel.setPrice(marketplaceRecordDto.price());

            return marketplaceRepository.save(existingProductModel);
        } else {
            throw new NotFoundException("Product with ID " + id);
        }
    }

    private boolean isAlreadyExisting(String existingName, String newName) {
        return !existingName.equals(newName) && marketplaceRepository.existsByName(newName);
    }




    @Transactional
    public MarketplaceModel save(MarketplaceModel marketplaceModel) {
        if (isAlreadyExisting(marketplaceModel)) {
            throw new DuplicatedException("Product with name '" + marketplaceModel.getName());
        }
        return marketplaceRepository.save(marketplaceModel);
    }

    private boolean isAlreadyExisting(MarketplaceModel marketplaceModel) {
        return marketplaceRepository.existsByName(marketplaceModel.getName());
    }

    @Transactional
    public List<MarketplaceModel> getAllProducts() {
        List<MarketplaceModel> productsList = marketplaceRepository.findAll();
        if (productsList.isEmpty()) {
            throw new NotFoundException("No products has been found");
        }
        return productsList;
    }

    @Transactional
    public Optional<MarketplaceModel> findById(UUID id) throws NotFoundException {
        Optional<MarketplaceModel> productO = marketplaceRepository.findById(id);

        if (productO.isEmpty()) {
            throw new NotFoundException("Product with ID " + id);
        }

        return marketplaceRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id) throws DeletedException, NotFoundException {
        Optional<MarketplaceModel> productO = marketplaceRepository.findById(id);
        if (productO.isEmpty()) {
            throw new NotFoundException("Product with ID " + id);
        }

        try {
            marketplaceRepository.delete(productO.get());
            throw new DeletedException("User with ID " + id + " has been deleted.");
        } catch (Exception e) {
            throw new DeletedException("Failed to delete user with ID " + id + ".");
        }
    }

}