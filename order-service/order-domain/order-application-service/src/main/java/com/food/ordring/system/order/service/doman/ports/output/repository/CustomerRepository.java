package com.food.ordring.system.order.service.doman.ports.output.repository;

import com.food.ordering.system.order.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findByCustomerId(UUID customerId);


}
