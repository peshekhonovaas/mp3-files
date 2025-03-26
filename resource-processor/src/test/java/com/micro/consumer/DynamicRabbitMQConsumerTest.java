package com.micro.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DynamicRabbitMQConsumerTest {

    @Mock
    private RabbitAdmin rabbitAdmin;

    @Mock
    private RabbitMQConfigFetcher configFetcher;

    @InjectMocks
    private DynamicRabbitMQConsumer dynamicRabbitMQConsumer;

    @Captor
    private ArgumentCaptor<TopicExchange> exchangeCaptor;

    @Captor
    private ArgumentCaptor<Queue> queueCaptor;

    @Captor
    private ArgumentCaptor<Binding> bindingCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void configureRabbitMQBindingsShouldDeclareExchangeQueueAndBinding() {
        // Arrange
        Map<String, String> config = Map.of(
                "exchangeName", "testExchange",
                "queueName", "testQueue",
                "routingKey", "testRoutingKey"
        );
        when(configFetcher.getRabbitMQConfig()).thenReturn(config);

        dynamicRabbitMQConsumer.configureRabbitMQBindings();

        verify(configFetcher, times(1)).getRabbitMQConfig();

        verify(rabbitAdmin, times(1)).declareExchange(exchangeCaptor.capture());
        TopicExchange capturedExchange = exchangeCaptor.getValue();
        assertEquals("testExchange", capturedExchange.getName());
        assertTrue(capturedExchange.isDurable());

        verify(rabbitAdmin, times(1)).declareQueue(queueCaptor.capture());
        Queue capturedQueue = queueCaptor.getValue();
        assertEquals("testQueue", capturedQueue.getName());
        assertTrue(capturedQueue.isDurable());

        verify(rabbitAdmin, times(1)).declareBinding(bindingCaptor.capture());
        Binding capturedBinding = bindingCaptor.getValue();
        assertEquals("testQueue", capturedBinding.getDestination());
        assertEquals("testRoutingKey", capturedBinding.getRoutingKey());
    }
}