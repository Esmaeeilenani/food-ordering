package com.food.ordring.system.order.service.doman;


import com.food.ordering.system.order.domain.OrderDomainService;
import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.entity.Restaurant;
import com.food.ordering.system.order.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.exception.OrderDomainException;
import com.food.ordring.system.order.service.doman.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.doman.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.doman.ports.output.repository.CustomerRepository;
import com.food.ordring.system.order.service.doman.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.doman.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;


    @Transactional
    public OrderCreatedEvent saveOrder(CreateOrderCommand createOrderCommand){
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.ceateOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);

        log.info("Order is created with id: {}", order.getId().getValue());
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);

        return restaurantRepository
                .findRestaurantInformation(restaurant)
                .orElseThrow(() -> new OrderDomainException("Could not find restaurant with id: " + restaurant.getId()));


    }

    private void checkCustomer(UUID customerId) {
        customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new OrderDomainException("Could not find customer with id: " + customerId));
    }

    private Order saveOrder(Order order) {
        Order newOrder = orderRepository.save(order);

        log.info("Order is saved with id: {}", newOrder.getId());

        return newOrder;
    }




}
