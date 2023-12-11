package com.marketplace.springboot.Service.Impl;

import com.marketplace.springboot.DTO.MakerteplaceRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedUserException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
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
    public MarketplaceModel update(UUID id, MakerteplaceRecordDto makerteplaceRecordDto) throws NotFoundException {
        Optional<MarketplaceModel> existingProduct = marketplaceRepository.findById(id);

        if (existingProduct.isPresent()) {
            MarketplaceModel updatedProduct = existingProduct.get();
            updatedProduct.setName(makerteplaceRecordDto.name());
            updatedProduct.setPrice(makerteplaceRecordDto.price());

            return marketplaceRepository.save(updatedProduct);
        } else {
            throw new NotFoundException("User with ID " + id);
        }
    }

    @Transactional
    public MarketplaceModel save(MarketplaceModel marketplaceModel) {
        return marketplaceRepository.save(marketplaceModel);
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
    public void delete(UUID id) throws DeletedUserException, NotFoundException {
        Optional<MarketplaceModel> productO = marketplaceRepository.findById(id);
        if (productO.isEmpty()) {
            throw new NotFoundException("Product with ID " + id);
        }

        try {
            marketplaceRepository.delete(productO.get());
            throw new DeletedUserException("User with ID " + id + " has been deleted.");
        } catch (Exception e) {
            throw new DeletedUserException("Failed to delete user with ID " + id + ".");
        }
    }

}


