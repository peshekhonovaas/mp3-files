package com.micro.consumer;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class DynamicRabbitMQConsumer {

    private final RabbitMQConfigFetcher configFetcher;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public DynamicRabbitMQConsumer(RabbitAdmin rabbitAdmin, RabbitMQConfigFetcher configFetcher) {
        this.configFetcher = configFetcher;
        this.rabbitAdmin = rabbitAdmin;
    }

    @PostConstruct
    public void configureRabbitMQBindings() {
        Map<String, String> config = this.configFetcher.getRabbitMQConfig();

        String exchangeName = config.get("exchangeName");
        String queueName = config.get("queueName");
        String routingKey = config.get("routingKey");

        TopicExchange exchange = new TopicExchange(exchangeName);
        Queue queue = new Queue(queueName, true);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);

        this.rabbitAdmin.declareExchange(exchange);
        this.rabbitAdmin.declareQueue(queue);
        this.rabbitAdmin.declareBinding(binding);
    }
}
