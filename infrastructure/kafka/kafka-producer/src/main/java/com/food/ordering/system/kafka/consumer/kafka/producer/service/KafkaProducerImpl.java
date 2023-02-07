package com.food.ordering.system.kafka.consumer.kafka.producer.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {


    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void send(String topicName, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback) {
        log.info("Sending message={} to topic={}", message, topicName);
        try {
            ListenableFuture<SendResult<K, V>> future = kafkaTemplate.send(topicName, key, message);
            future.addCallback(callback);
        } catch (Exception e) {
            log.error("Error while sending message to topic={} with message={} exception: {}"
                    , topicName, message, e.getMessage());
            throw new KafkaException("Error while sending message to topic=" + topicName, e);
        }

    }


    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            kafkaTemplate.destroy();
        }
    }


}
