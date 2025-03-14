package com.micro.messagebroker;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
public class RabbitMQConfig {
    public final String resourceExchangeName;
    public final String resourceQueueName;
    public final String resourceRoutingKey;

    public String getResourceExchangeName() {
        return this.resourceExchangeName;
    }

    public String getResourceQueueName() {
        return this.resourceQueueName;
    }

    public String getResourceRoutingKey() {
        return this.resourceRoutingKey;
    }

    public RabbitMQConfig(@Value("${rabbitmq.resource.exchange-name}") String resourceExchangeName,
                          @Value("${rabbitmq.resource.queue-name}") String resourceQueueName,
                          @Value("${rabbitmq.resource.routing-key}") String resourceRoutingKey) {
        this.resourceExchangeName = resourceExchangeName;
        this.resourceQueueName = resourceQueueName;
        this.resourceRoutingKey = resourceRoutingKey;
    }

    @Bean
    public TopicExchange resourceExchange() {
        return new TopicExchange(this.resourceExchangeName);
    }

    @Bean
    public Queue resourceQueue() {
        return new Queue(this.resourceQueueName, true);
    }

    @Bean
    public Binding resourceBinding(Queue resourceQueue, TopicExchange resourceExchange) {
        return BindingBuilder.bind(resourceQueue)
                .to(resourceExchange)
                .with(this.resourceRoutingKey);
    }
}
