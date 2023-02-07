package com.food.ordering.system.order.service.dataaccess.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@IdClass(OrderItemEntityId.class)
public class OrderItemEntity {

    @Id
    private Long id;


    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    private UUID productId;

    private BigDecimal price;
    private Integer quantity;

    private BigDecimal subTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return id.equals(that.id) && orderEntity.equals(that.orderEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderEntity);
    }
}