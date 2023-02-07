package com.food.ordering.system.order.domain.entity;

import com.food.ordering.system.entity.AggregateRoot;
import com.food.ordering.system.order.domain.exception.OrderDomainException;
import com.food.ordering.system.order.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.domain.valueobject.TrackingId;
import com.food.ordering.system.valueobject.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;

    private final StreetAddress streetAddress;
    private Money price;

    private List<OrderItem> items;

    private TrackingId trackingId;

    private OrderStatus status;

    private List<String> failureMessages;


    private Order(Builder builder) {
        super.setId(builder.id);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        streetAddress = builder.deliverAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        status = builder.status;
        failureMessages = builder.failureMessages;
    }


    public static Builder builder() {
        return Builder.builder();
    }

    public void initiateOrder() {
        setId(new OrderId(UUID.randomUUID()));
        this.trackingId = new TrackingId(UUID.randomUUID());
        this.status = OrderStatus.PENDING;

        initOrderItems();

    }

    private void initOrderItems() {
        long itemId = 1;

        for (OrderItem item : items) {
            item.initOrderItem(this.getId(), itemId++);
        }

    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();

    }


    private void validateInitialOrder() {
        if (status != null || getId() != null) {
            throw new OrderDomainException("Order is already initiated");
        }


    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Order price is not valid");
        }

    }

    private void validateItemsPrice() {
        Money orderTotalPrice = items.stream()
                .map(orderItem -> {
                    validateItemPrice(orderItem);
                    return orderItem.getSubTotal();
                }).reduce(Money.ZERO, Money::add);

        if (!price.equals(orderTotalPrice)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                    + " is not equal to Order items total: " + orderTotalPrice.getAmount());
        }

    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
                    " is not valid for product: " + orderItem.getProduct().getName() +
                    ", number: " + orderItem.getProduct().getId().getValue());
        }
    }


    public void pay() {
        if (status != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct status for payment");
        }
        this.status = OrderStatus.PAID;
    }

    public void approve() {
        if (status != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not payed yet for approval");
        }
        this.status = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (status != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not payed yet for cancellation");
        }
        this.status = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }


    public void cancel(List<String> failureMessages) {

        if (!(status == OrderStatus.PENDING || status == OrderStatus.CANCELLING)) {
            throw new OrderDomainException("Order is not payed yet for cancellation");
        }
        this.status = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {


        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(m-> !m.isEmpty()).toList());
        }

        if (this.failureMessages == null) {
            this.failureMessages = failureMessages.stream().filter(m-> !m.isEmpty()).toList();
        }

    }


    public static final class Builder {
        private OrderId id;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliverAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus status;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(OrderId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliverAddress(StreetAddress val) {
            deliverAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder status(OrderStatus val) {
            status = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
