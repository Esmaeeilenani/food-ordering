package com.food.ordering.system.order.service.messaging.publisher.kafka;


import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public class OrderKafkaMessageHelper {

    public static <T> ListenableFutureCallback<SendResult<String, T>> getKafkaCallBack(String responseTopicName, T requestAvroModel,String orderId) {
        return new ListenableFutureCallback<SendResult<String, T>>() {

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();

                log.info("Received successful response from kafka for order id: {}" +
                                " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());

            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending {} message: {} to topic: {}",
                        requestAvroModel.getClass().getName(),
                        requestAvroModel.toString(), responseTopicName, ex);

            }
        };

    }


}
