package com.food.ordring.system.order.service.doman;

import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.domain.valueobject.TrackingId;
import com.food.ordring.system.order.service.doman.dto.track.TrackOrderQuery;
import com.food.ordring.system.order.service.doman.dto.track.TrackOrderResponse;
import com.food.ordring.system.order.service.doman.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.doman.ports.output.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;

    private OrderRepository orderRepository;


    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {

     Order order =   orderRepository
                .findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()))
                .orElseThrow(()-> new OrderNotFoundException("can't find order with tracking id: " + trackOrderQuery.getOrderTrackingId()));



        return orderDataMapper.orderToTrackOrderResponse(order,"Order created successfully");

    }


}
