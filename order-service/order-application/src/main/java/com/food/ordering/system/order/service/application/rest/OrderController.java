package com.food.ordering.system.order.service.application.rest;

import com.food.ordring.system.order.service.doman.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.doman.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.doman.dto.track.TrackOrderQuery;
import com.food.ordring.system.order.service.doman.dto.track.TrackOrderResponse;
import com.food.ordring.system.order.service.doman.ports.input.service.OrderApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderApplicationService orderApplicationService;


    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderCommand createOrderCommand) {
        log.info("creating order for customer: {} at restaurant: {}", createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("order created with tracking  id: {}", createOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(createOrderResponse);


    }


    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.info("Order status: {} for tracking id: {}", trackOrderResponse.getOrderStatus(),trackingId.toString());
        return ResponseEntity.ok(trackOrderResponse);

    }


}
