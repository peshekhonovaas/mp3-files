package com.micro.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProducerServiceImplTest {

    @InjectMocks
    private ProducerServiceImpl producerService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RabbitMQConfigFetcher configFetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConfigFetcherInvocation() {
        Map<String, String> mockConfig = Map.of(
                "exchangeName", "testExchange",
                "routingKey", "testRoutingKey"
        );
        when(configFetcher.getConfig()).thenReturn(mockConfig);
        Map<String, String> result = configFetcher.getConfig();
        assertEquals("testExchange", result.get("exchangeName"));
        verify(configFetcher, times(1)).getConfig();
    }
}