package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.consumer.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.order.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordring.system.order.service.doman.config.OrderServiceConfigData;
import com.food.ordring.system.order.service.doman.ports.output.message.publisher.payment.OrderCreatedPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderKafkaPublisher implements OrderCreatedPublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;

    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCreatedEvent for order id: {}", orderId);


        PaymentRequestAvroModel paymentRequestAvroModel = null;
        try {
            paymentRequestAvroModel = orderMessagingDataMapper
                    .orderCreatedEventToPaymentRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(), orderId,
                    paymentRequestAvroModel, OrderKafkaMessageHelper
                            .getKafkaCallBack(orderServiceConfigData.getPaymentResponseTopicName(),
                                    paymentRequestAvroModel, orderId));


            log.info("PaymentRequestAvroModel message: {} sent to topic: {} with order id: {}",
                    paymentRequestAvroModel.toString(), orderServiceConfigData.getPaymentRequestTopicName()
                    , paymentRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message: {} to topic: {} " +
                            "with order id: {} Error: {}",
                    paymentRequestAvroModel,
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId, e.getMessage());
        }

    }

}
