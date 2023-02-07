package com.food.ordring.system.order.service.doman.ports.output.repository;

import com.food.ordering.system.order.domain.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CustomerRepository {

    Optional<Customer> findByCustomerId(UUID customerId);


}
