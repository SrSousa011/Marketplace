package com.marketplace.springboot.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.marketplace.springboot.Dto.OrderRecordDto;
import com.marketplace.springboot.Exception.Impl.DeletedException;
import com.marketplace.springboot.Exception.Impl.NotFoundException;
import com.marketplace.springboot.Model.OrderModel;
import com.marketplace.springboot.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Marketplace API REST", description = "Endpoints for managing marketplace orders.")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(
            @PathVariable(value = "id") UUID id,
            @RequestBody @Valid OrderRecordDto orderRecordDto
    ) {
        try {
            OrderModel updatedOrder = orderService.updateOrder(id, orderRecordDto);
            logger.info("Order with ID {} successfully updated.", id);
            return ResponseEntity.ok(updatedOrder);
        } catch (NotFoundException e) {
            logger.error("Order update failed. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderModel> ordersList = orderService.getAllOrders();
            List<EntityModel<OrderModel>> ordersWithLinks = new ArrayList<>();

            for (OrderModel order : ordersList) {
                UUID orderId = order.getOrderEntityId();
                EntityModel<OrderModel> orderWithLink = EntityModel.of(order);
                orderWithLink.add(linkTo(methodOn(OrderController.class).getOneOrder(orderId)).withSelfRel());
                ordersWithLinks.add(orderWithLink);
            }

            CollectionModel<EntityModel<OrderModel>> collectionModel = CollectionModel.of(ordersWithLinks);
            collectionModel.add(linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());

            logger.info("Retrieved a list of all orders.");
            return ResponseEntity.status(HttpStatus.OK).body(collectionModel);

        } catch (NotFoundException e) {
            logger.error("Error retrieving orders. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneOrder(@PathVariable(value = "id") UUID id) {
        try {
            Optional<OrderModel> order = orderService.getOrderById(id);

            if (order.isPresent()) {
                logger.info("Retrieved details of order with ID {}", id);
                EntityModel<Optional<OrderModel>> resource = EntityModel.of(order);
                resource.add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("Orders List"));
                return ResponseEntity.ok(resource);
            } else {
                logger.warn("Order not found for ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found for ID: " + id);
            }
        } catch (NotFoundException e) {
            logger.error("Error retrieving order details. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Save a new order with the provided details.")
    public ResponseEntity<OrderModel> saveOrder(@RequestBody @Valid OrderRecordDto orderRecordDto) {
        try {
            var orderModel = getOrderModel(orderRecordDto);

            OrderModel savedOrder = orderService.saveOrder(orderModel);
            logger.debug("Order saved successfully: {}", savedOrder);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (Exception e) {
            logger.error("Error saving order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable(value = "id") UUID id) {
        try {
            orderService.deleteOrder(id);
            logger.info("Order with ID {} successfully deleted.", id);
            return ResponseEntity.status(HttpStatus.OK).body("Order with ID " + id + " successfully deleted.");
        } catch (NotFoundException e) {
            logger.error("Order deletion failed. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DeletedException e) {
            logger.error("Order deletion failed. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ExceptionHandler({NotFoundException.class, DeletedException.class})
    public ResponseEntity<Object> handleExceptions(Exception e) {
        logger.error("Exception occurred", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    private static OrderModel getOrderModel(OrderRecordDto orderRecordDto) {
        return OrderModel.builder()
                .status(orderRecordDto.getStatus())
                .orderDate(orderRecordDto.getOrderDate())
                .build();
    }
}
