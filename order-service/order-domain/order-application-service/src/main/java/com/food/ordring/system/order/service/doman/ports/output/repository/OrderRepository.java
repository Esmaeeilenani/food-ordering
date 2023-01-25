package com.food.ordring.system.order.service.doman.ports.output.repository;

import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);


}
