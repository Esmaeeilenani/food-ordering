package com.food.ordring.system.order.service.doman;
import com.food.ordring.system.order.service.doman.dto.message.RestaurantApprovalResponse;
import com.food.ordring.system.order.service.doman.ports.input.message.listener.restaurantapproval.RestaurantListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantListenerImpl implements RestaurantListener {



    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}

