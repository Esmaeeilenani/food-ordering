package com.food.ordring.system.order.service.doman.ports.output.repository;

import com.food.ordering.system.order.domain.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);


}
