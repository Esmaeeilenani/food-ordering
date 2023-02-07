package com.food.ordring.system.order.service.doman.ports.output.message.publisher.payment;

import com.food.ordering.system.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.event.OrderCreatedEvent;

public interface OrderCreatedPublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
