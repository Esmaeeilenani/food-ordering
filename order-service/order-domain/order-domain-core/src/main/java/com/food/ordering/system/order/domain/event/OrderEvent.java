package com.food.ordering.system.order.domain.event;

import com.food.ordering.system.event.DomainEvent;
import com.food.ordering.system.order.domain.entity.Order;
import lombok.Getter;

import java.time.ZonedDateTime;
@Getter
abstract class OrderEvent implements DomainEvent<Order> {


    private final Order order;
    private final ZonedDateTime createdAt;

    public OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }


}
