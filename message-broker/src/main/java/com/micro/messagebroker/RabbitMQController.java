package com.micro.messagebroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/broker")
public class RabbitMQController {
    private final RabbitMQConfig rabbitMQConfig;

    @Autowired
    public RabbitMQController(RabbitMQConfig rabbitMQConfig) {
        this.rabbitMQConfig = rabbitMQConfig;
    }
    @GetMapping("/rabbitmq/config")
    public Map<String, String> getRabbitMQConfig() {
        return Map.of(
                "exchangeName", this.rabbitMQConfig.getResourceExchangeName(),
                "queueName", this.rabbitMQConfig.getResourceQueueName(),
                "routingKey", this.rabbitMQConfig.getResourceRoutingKey()
        );
    }
}
