package com.food.ordring.system.order.service.doman.ports.output.message.publisher.restaurantapproval;

import com.food.ordering.system.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.event.OrderPaidEvent;

public interface OrderPaidPublisher extends DomainEventPublisher<OrderPaidEvent> {



}
