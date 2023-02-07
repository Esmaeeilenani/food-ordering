package com.food.ordering.system.order.domain;

import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.exception.OrderDomainException;
import com.food.ordering.system.order.domain.entity.Product;
import com.food.ordering.system.order.domain.entity.Restaurant;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProducts(order, restaurant);
        order.validateOrder();
        order.initiateOrder();
        log.info("Order with id: {} is initiated successfully", order.getId().getValue());
        return new OrderCreatedEvent(order,
                ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void setOrderProducts(Order order, Restaurant restaurant) {

        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();

            restaurant.getProducts().forEach(restaurantProduct -> {
                if (restaurantProduct.equals(currentProduct)) {
                    currentProduct.updateWithConfirmNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
                }
            });

        });

    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() + " is not active");
        }


    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved successfully", order.getId().getValue());
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid successfully", order.getId().getValue());
        return new OrderPaidEvent(order,
                ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initiateOrder();
        log.info("Order payment is cancelling for order id: {} ", order.getId().getValue());
        return new OrderCancelledEvent(order,
                ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order is canceled for order id: {} ", order.getId().getValue());
    }
}
