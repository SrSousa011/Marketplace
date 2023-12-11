package com.marketplace.springboot.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marketplace.springboot.Model.MarketplaceModel;

@Repository
public interface MarketplaceRepository extends JpaRepository<MarketplaceModel, UUID> {

}
