package com.food.ordring.system.order.service.doman.ports.input.message.listener.restaurantapproval;

import com.food.ordring.system.order.service.doman.dto.message.RestaurantApprovalResponse;

public interface RestaurantListener {
    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
