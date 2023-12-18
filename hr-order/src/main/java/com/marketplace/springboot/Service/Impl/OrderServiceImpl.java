package com.marketplace.springboot.Service.Impl;

import com.marketplace.springboot.Dto.OrderRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.OrderModel;
import com.marketplace.springboot.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderModel updateOrder(UUID id, @Valid OrderRecordDto orderRecordDto) throws NotFoundException {
        Optional<OrderModel> existingOrder = orderRepository.findById(id);

        if (existingOrder.isPresent()) {
            OrderModel existingOrderModel = existingOrder.get();
            // Perform any necessary validation or business logic here
            existingOrderModel.setStatus(orderRecordDto.getStatus());
            existingOrderModel.setOrderDate(orderRecordDto.getOrderDate());

            return orderRepository.save(existingOrderModel);
        } else {
            throw new NotFoundException("Order with ID " + id);
        }
    }

    @Transactional
    public OrderModel saveOrder(OrderModel orderModel) {
        return orderRepository.save(orderModel);
    }

    @Transactional
    public List<OrderModel> getAllOrders() {
        List<OrderModel> ordersList = orderRepository.findAll();
        if (ordersList.isEmpty()) {
            throw new NotFoundException("No orders have been found");
        }
        return ordersList;
    }

    @Transactional
    public Optional<OrderModel> getOrderById(UUID id) throws NotFoundException {
        Optional<OrderModel> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            throw new NotFoundException("Order with ID " + id);
        }

        return orderOptional;
    }

    @Transactional
    public void deleteOrder(UUID id) throws DeletedException, NotFoundException {
        Optional<OrderModel> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException("Order with ID " + id);
        }

        try {
            orderRepository.delete(orderOptional.get());
            throw new DeletedException("Order with ID " + id + " has been deleted.");
        } catch (Exception e) {
            throw new DeletedException("Failed to delete order with ID " + id + ".");
        }
    }
}
