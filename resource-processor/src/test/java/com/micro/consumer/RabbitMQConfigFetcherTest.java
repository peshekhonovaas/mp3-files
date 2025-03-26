package com.micro.consumer;

import com.micro.exception.ConfigurationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RabbitMQConfigFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RabbitMQConfigFetcher rabbitMQConfigFetcher;

    @Captor
    private ArgumentCaptor<ParameterizedTypeReference<Map<String, String>>> typeReferenceCaptor;

    private static final String RABBIT_MQ_MANAGER_URL = "http://mock-rabbitmq-manager-url";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rabbitMQConfigFetcher = new RabbitMQConfigFetcher(restTemplate, RABBIT_MQ_MANAGER_URL);
    }

    @Test
    void fetchRabbitMQConfigShouldUpdateCachedConfigWhenValidResponse() {
        Map<String, String> mockConfig = Map.of(
                "exchangeName", "testExchange",
                "queueName", "testQueue",
                "routingKey", "testRoutingKey"
        );
        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok(mockConfig);

        when(restTemplate.exchange(
                eq(RABBIT_MQ_MANAGER_URL),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseEntity);

        rabbitMQConfigFetcher.fetchRabbitMQConfig();

        Map<String, String> result = rabbitMQConfigFetcher.getRabbitMQConfig();
        assertNotNull(result);
        assertEquals(mockConfig.size(), result.size());
        assertEquals(mockConfig.get("exchangeName"), result.get("exchangeName"));
        assertEquals(mockConfig.get("queueName"), result.get("queueName"));
        assertEquals(mockConfig.get("routingKey"), result.get("routingKey"));

        verify(restTemplate, times(1)).exchange(
                eq(RABBIT_MQ_MANAGER_URL),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void fetchRabbitMQConfigShouldThrowConfigurationFailedExceptionOnException() {
        when(restTemplate.exchange(
                eq(RABBIT_MQ_MANAGER_URL),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenThrow(new RuntimeException("Simulated RestTemplate exception"));

        Exception exception = assertThrows(
                ConfigurationFailedException.class,
                rabbitMQConfigFetcher::fetchRabbitMQConfig,
                "Expected ConfigurationFailedException due to RestTemplate failure"
        );

        assertEquals("Failed to fetch RabbitMQ configuration", exception.getMessage());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof RuntimeException);

        verify(restTemplate, times(1)).exchange(
                eq(RABBIT_MQ_MANAGER_URL),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void getRabbitMQConfigShouldFetchOnFirstCallWhenCachedConfigIsEmpty() {
        Map<String, String> mockConfig = Map.of(
                "exchangeName", "testExchange",
                "queueName", "testQueue",
                "routingKey", "testRoutingKey"
        );
        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok(mockConfig);

        when(restTemplate.exchange(
                eq(RABBIT_MQ_MANAGER_URL),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseEntity);

        Map<String, String> result = rabbitMQConfigFetcher.getRabbitMQConfig();

        assertNotNull(result);
        assertEquals(mockConfig.size(), result.size());
        assertEquals(mockConfig.get("exchangeName"), result.get("exchangeName"));
        assertEquals(mockConfig.get("queueName"), result.get("queueName"));
        assertEquals(mockConfig.get("routingKey"), result.get("routingKey"));

        verify(restTemplate, times(1)).exchange(
                eq(RABBIT_MQ_MANAGER_URL),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );
    }
}