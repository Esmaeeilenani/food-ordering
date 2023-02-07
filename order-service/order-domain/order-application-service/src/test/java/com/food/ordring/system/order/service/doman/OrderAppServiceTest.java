package com.food.ordring.system.order.service.doman;

import com.food.ordering.system.order.domain.entity.Customer;
import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.entity.Product;
import com.food.ordering.system.order.domain.entity.Restaurant;
import com.food.ordering.system.order.domain.exception.OrderDomainException;
import com.food.ordering.system.valueobject.*;
import com.food.ordring.system.order.service.doman.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.doman.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.doman.dto.create.OrderAddress;
import com.food.ordring.system.order.service.doman.dto.create.OrderItem;
import com.food.ordring.system.order.service.doman.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.doman.ports.input.service.OrderApplicationService;
import com.food.ordring.system.order.service.doman.ports.output.repository.CustomerRepository;
import com.food.ordring.system.order.service.doman.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.doman.ports.output.repository.RestaurantRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = OrderTestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderAppServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;

    private final UUID CUSTOMER_ID = UUID.fromString("c0a80101-0000-0000-0000-000000000001");
    private final UUID RESTAURANT_ID = UUID.fromString("c0a80101-0000-0000-0000-000000000002");
    private final UUID PRODUCT_ID = UUID.fromString("c0a80101-0000-0000-0000-000000000003");
    private final UUID ORDER_ID = UUID.fromString("c0a80101-0000-0000-0000-000000000004");

    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init() {

        createOrderCommand = CreateOrderCommand.builder().customerId(CUSTOMER_ID).restaurantId(RESTAURANT_ID).address(OrderAddress.builder().street("street").postalCode("12345").city("city").build()).price(PRICE).items(List.of(OrderItem.builder().productId(PRODUCT_ID).quantity(1).price(new BigDecimal("50.00")).subTotal(new BigDecimal("50.00")).build(), OrderItem.builder().productId(PRODUCT_ID).quantity(3).price(new BigDecimal("50.00")).subTotal(new BigDecimal("150.00")).build())).build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder().customerId(CUSTOMER_ID).restaurantId(RESTAURANT_ID).address(OrderAddress.builder().street("street").postalCode("12345").city("city").build()).price(new BigDecimal("2500.00")).items(List.of(OrderItem.builder().productId(PRODUCT_ID).quantity(1).price(new BigDecimal("50.00")).subTotal(new BigDecimal("50.00")).build(), OrderItem.builder().productId(PRODUCT_ID).quantity(3).price(new BigDecimal("50.00")).subTotal(new BigDecimal("150.00")).build())).build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder().customerId(CUSTOMER_ID).restaurantId(RESTAURANT_ID).address(OrderAddress.builder().street("street").postalCode("12345").city("city").build()).price(new BigDecimal("2100.00")).items(List.of(OrderItem.builder().productId(PRODUCT_ID).quantity(1).price(new BigDecimal("60.00")).subTotal(new BigDecimal("60.00")).build(), OrderItem.builder().productId(PRODUCT_ID).quantity(3).price(new BigDecimal("50.00")).subTotal(new BigDecimal("150.00")).build())).build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurant = Restaurant.builder().id(new RestaurantId(RESTAURANT_ID)).active(true).products(List.of(new Product(new ProductId(PRODUCT_ID)).setName("product-1").setPrice(new Money(new BigDecimal("50.00"))), new Product(new ProductId(PRODUCT_ID)).setName("product-2").setPrice(new Money(new BigDecimal("50.00"))))).build();

        Order order = orderDataMapper.ceateOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));


        Mockito.when(customerRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        Mockito.when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))).thenReturn(Optional.of(restaurant));

        Mockito.when(orderRepository.save(order)).thenReturn(order);

    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(createOrderResponse.getOrderStatus(), OrderStatus.PENDING);
        assertEquals(createOrderResponse.getMessage(), "Order created successfully");
        assertNotNull(createOrderResponse.getOrderTrackingId());

    }

    @Test
    public void testCreateOrderWrongTotalPrice() {
        assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));

    }

    @Test
    public void testCreateOrderWrongProductPrice() {
        assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));

    }


}
