package com.food.ordring.system.order.service.doman;

import com.food.ordering.system.order.domain.OrderDomainService;
import com.food.ordering.system.order.domain.OrderDomainServiceImpl;
import com.food.ordring.system.order.service.doman.ports.output.message.publisher.payment.OrderCancelledPublisher;
import com.food.ordring.system.order.service.doman.ports.output.message.publisher.payment.OrderCreatedPublisher;
import com.food.ordring.system.order.service.doman.ports.output.message.publisher.restaurantapproval.OrderPaidPublisher;
import com.food.ordring.system.order.service.doman.ports.output.repository.CustomerRepository;
import com.food.ordring.system.order.service.doman.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.doman.ports.output.repository.RestaurantRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.food.ordring.system.order.service.doman"})
class OrderTestConfig {


    @Bean
    public OrderCreatedPublisher orderCreatedPublisher() {
        return Mockito.mock(OrderCreatedPublisher.class);
    }

    @Bean
    public OrderCancelledPublisher orderCancelledPublisher() {
        return Mockito.mock(OrderCancelledPublisher.class);
    }

    @Bean
    public OrderPaidPublisher orderPaidPublisher() {
        return Mockito.mock(OrderPaidPublisher.class);
    }

    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return Mockito.mock(RestaurantRepository.class);
    }

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }


}