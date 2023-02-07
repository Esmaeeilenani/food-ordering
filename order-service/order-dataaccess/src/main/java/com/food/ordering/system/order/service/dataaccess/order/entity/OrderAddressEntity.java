package com.food.ordering.system.order.service.dataaccess.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class OrderAddressEntity {

    @Id
    private UUID id;
    @JoinColumn(name = "order_id")
    @OneToOne(cascade = CascadeType.ALL)
    private OrderEntity order;
    private String street;
    private String postalCode;
    private String city;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAddressEntity that = (OrderAddressEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
