package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.entity.OrderItem;
import com.food.ordering.system.order.domain.entity.Product;
import com.food.ordering.system.order.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.domain.valueobject.TrackingId;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.valueobject.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderDataAccessMapper {


    public OrderEntity orderToOrderEntity(Order order) {
        String failureMessage = order.getFailureMessages() != null ?
                String.join(",", order.getFailureMessages()) : "";
        return new OrderEntity()
                .id(order.getId().getValue())
                .trackingId(order.getTrackingId().getId())
                .orderStatus(order.getStatus())
                .customerId(order.getCustomerId().getValue())
                .address(deliveryAddressToAddressEntity(order.getStreetAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemEntities(order.getItems()))
                .orderStatus(order.getStatus())
                .failureMessage(failureMessage);


    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .id(new OrderId(orderEntity.id()))
                .customerId(new CustomerId(orderEntity.customerId()))
                .restaurantId(new RestaurantId(orderEntity.restaurantId()))
                .deliverAddress(addressEntityDeliverAddress(orderEntity.address()))
                .price(new Money(orderEntity.price()))
                .items(orderItemEntitiesToOrderItems(orderEntity.items()))
                .trackingId(new TrackingId(orderEntity.trackingId()))
                .status(orderEntity.orderStatus())
                .failureMessages(Arrays.stream(orderEntity.failureMessage().split(",")).toList())
                .build();

    }

    private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(this::orderItemEntityToOrderItems)
                .toList();
    }

    private OrderItem orderItemEntityToOrderItems(OrderItemEntity item) {
        return OrderItem.builder()
                .id(new OrderItemId(item.id()))
//                .orderId(new OrderId(item.orderEntity().id()))
                .product(new Product(new ProductId(item.productId())))
                .price(new Money(item.price()))
                .quantity(item.quantity())
                .subTotal(new Money(item.subTotal()))
                .build();
    }

    private StreetAddress addressEntityDeliverAddress(OrderAddressEntity address) {
        return new StreetAddress(address.id(), address.street(), address.city(), address.postalCode());
    }


    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> items) {
        return items
                .stream()
                .map(this::orderItemToOrderItemEntity)
                .toList();
    }

    private OrderItemEntity orderItemToOrderItemEntity(OrderItem item) {
        return new OrderItemEntity()
                .id(item.getId().getValue())
                .productId(item.getProduct().getId().getValue())
                .price(item.getPrice().getAmount())
                .quantity(item.getQuantity())
                .subTotal(item.getSubTotal().getAmount());
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress streetAddress) {
        return new OrderAddressEntity()
                .id(streetAddress.getUuid())
                .city(streetAddress.getCity())
                .postalCode(streetAddress.getPostalCode())
                .street(streetAddress.getStreet());
    }


}
