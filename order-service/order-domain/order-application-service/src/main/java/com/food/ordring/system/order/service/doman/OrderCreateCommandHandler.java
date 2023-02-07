package com.food.ordring.system.order.service.doman;

import com.food.ordering.system.order.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.doman.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.doman.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.doman.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.doman.ports.output.message.publisher.payment.OrderCreatedPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPublisher orderCreatedPublisher;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.saveOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        orderCreatedPublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(),"Order created successfully");
    }


}
