package com.marketplace.springboot.Service;

import com.marketplace.springboot.Dto.OrderRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.OrderModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    OrderModel updateOrder(UUID id, OrderRecordDto marketplaceRecordDto) throws NotFoundException;

    OrderModel saveOrder(OrderModel marketplaceModel);

    List<OrderModel> getAllOrders() throws NotFoundException;

    Optional<OrderModel> getOrderById(UUID id) throws NotFoundException;

    void deleteOrder(UUID id) throws DeletedException, NotFoundException;
}
