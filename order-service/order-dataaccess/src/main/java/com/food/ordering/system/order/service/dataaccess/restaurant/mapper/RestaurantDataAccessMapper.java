package com.food.ordering.system.order.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.order.domain.entity.Product;
import com.food.ordering.system.order.domain.entity.Restaurant;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.valueobject.Money;
import com.food.ordering.system.valueobject.ProductId;
import com.food.ordering.system.valueobject.RestaurantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {


    public List<UUID> restaurantToRestaurantProduct(Restaurant restaurant) {

        return restaurant.getProducts()
                .stream().map(product -> product.getId().getValue()).toList();

    }


    public Restaurant restaurantEntitiesToRestaurant(List<RestaurantEntity> restaurantEntities) {
    RestaurantEntity restaurantEntity = restaurantEntities.stream()
                .findFirst()
                .orElseThrow(() -> new RestaurantDataAccessException("Restaurant not found"));

    List<Product> restaurantProducts = restaurantEntities.stream()
                .map(restaurant -> new Product(new ProductId(restaurant.getProductId()),
                        restaurant.getProductName(), new Money(restaurant.getProductPrice())))
                .toList();

        return Restaurant.builder()
                .id(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }


}
