package com.food.ordering.system.order.domain.entity;

import com.food.ordering.system.entity.AggregateRoot;
import com.food.ordering.system.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer() {

    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }


}
