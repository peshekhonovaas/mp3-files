package com.micro.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

@Service
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfigFetcher configFetcher;

    @Autowired
    public ProducerServiceImpl(RabbitTemplate rabbitTemplate, RabbitMQConfigFetcher configFetcher) {
        this.rabbitTemplate = rabbitTemplate;
        this.configFetcher = configFetcher;
    }

    public void sendResourceId(String resourceId) {
        Map<String, String> rabbitMQConfig = configFetcher.getConfig();
        String exchangeName = rabbitMQConfig.get("exchangeName");
        String routingKey = rabbitMQConfig.get("routingKey");
        this.rabbitTemplate.convertAndSend(exchangeName, routingKey, resourceId);
    }
}
