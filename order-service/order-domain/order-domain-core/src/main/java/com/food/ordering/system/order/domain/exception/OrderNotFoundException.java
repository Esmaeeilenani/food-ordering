package com.food.ordering.system.order.domain.exception;

public class OrderNotFoundException extends OrderDomainException{
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
