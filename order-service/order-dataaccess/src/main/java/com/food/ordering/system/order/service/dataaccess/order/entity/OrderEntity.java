package com.food.ordering.system.order.service.dataaccess.order.entity;

import com.food.ordering.system.valueobject.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class OrderEntity {

    @Id
    private UUID id;

    private UUID customerId;

    private UUID restaurantId;
    private UUID trackingId;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String failureMessage;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddressEntity address;


    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items;


    public OrderEntity items(List<OrderItemEntity> orderItems) {
        this.items = orderItems;
        if (this.items != null) {
            this.items.forEach(item -> item.orderEntity(this));
        }
        return this;

    }


    public OrderEntity address(OrderAddressEntity address) {
        this.address = address;
        if (this.address != null) {
            this.address.order(this);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
