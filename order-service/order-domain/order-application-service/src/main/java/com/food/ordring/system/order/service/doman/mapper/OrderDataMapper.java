package com.food.ordring.system.order.service.doman.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.entity.OrderItem;
import com.food.ordering.system.order.domain.entity.Product;
import com.food.ordering.system.order.domain.entity.Restaurant;
import com.food.ordering.system.order.domain.valueobject.StreetAddress;
import com.food.ordring.system.order.service.doman.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.doman.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.doman.dto.create.OrderAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        List<Product> products = createOrderCommand.getItems().stream()
                .map(item -> new Product(new ProductId(item.getProductId())))
                .toList();

        return Restaurant.builder()
                .id(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(products)
                .build();
    }

    public Order ceateOrderCommandToOrder(CreateOrderCommand createOrderCommand) {

        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliverAddress(orderAddressToDeliverAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsDtoToEntity(createOrderCommand.getItems()))
                .build();
    }

    private List<OrderItem> orderItemsDtoToEntity(List<com.food.ordring.system.order.service.doman.dto.create.OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(this::orderItemsDtoToEntity)
                .toList();
    }

    private OrderItem orderItemsDtoToEntity(com.food.ordring.system.order.service.doman.dto.create.OrderItem orderItem) {
        return OrderItem.builder()
                .product(new Product(new ProductId(orderItem.getProductId()) ))
                .price(new Money(orderItem.getPrice()))
                .quantity(orderItem.getQuantity())
                .subTotal(new Money(orderItem.getSubTotal()))
                .build();
    }


    private StreetAddress orderAddressToDeliverAddress(OrderAddress address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity()
        );
    }


    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getId())
                .orderStatus(order.getStatus())
                .build();
    }
}
