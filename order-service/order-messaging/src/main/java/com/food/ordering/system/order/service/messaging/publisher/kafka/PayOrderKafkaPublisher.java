package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.consumer.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.order.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordring.system.order.service.doman.config.OrderServiceConfigData;
import com.food.ordring.system.order.service.doman.ports.output.message.publisher.restaurantapproval.OrderPaidPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayOrderKafkaPublisher implements OrderPaidPublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;

    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

    @Override
    public void publish(OrderPaidEvent domainEvent) {

        String orderId = domainEvent.getOrder().getId().getValue().toString();


        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper
                    .orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName()
                    , orderId
                    , restaurantApprovalRequestAvroModel,
                    OrderKafkaMessageHelper.getKafkaCallBack(
                            orderServiceConfigData.getRestaurantApprovalResponseTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderId)
            );

            log.info("RestaurantApprovalRequestAvroModel message: {} sent to topic: {} with order id: {}",
                    restaurantApprovalRequestAvroModel.toString(),
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    restaurantApprovalRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalRequestAvroModel message to topic: {} " +
                            "with order id: {} Error: {}",
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    orderId,
                    e.getMessage());
        }

    }
}
