package com.food.ordring.system.order.service.doman.ports.input.message.listener.payment;

import com.food.ordring.system.order.service.doman.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}
