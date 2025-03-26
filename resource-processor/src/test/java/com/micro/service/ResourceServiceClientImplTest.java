package com.micro.service;

import com.micro.exception.MessageFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceServiceClientImplTest {

    @Mock
    private RestTemplate loadBalancedRestTemplate;

    @Value("${resource.service.url}")
    private String resourceServiceUrl;

    @InjectMocks
    private ResourceServiceClientImpl resourceServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetResourceDataSuccess() {
        Long resourceId = 1L;
        byte[] expectedResponse = "test data".getBytes();

        when(loadBalancedRestTemplate.getForObject(resourceServiceUrl + "/{id}", byte[].class, resourceId))
                .thenReturn(expectedResponse);

        byte[] response = resourceServiceClient.getResourceData(resourceId);

        assertArrayEquals(expectedResponse, response);
    }

    @Test
    void testGetResourceDataHttpServerErrorException() {
        Long resourceId = 2L;

        when(loadBalancedRestTemplate.getForObject(resourceServiceUrl + "/{id}", byte[].class, resourceId))
                .thenThrow(HttpServerErrorException.class);
        assertThrows(
                HttpServerErrorException.class,
                () -> resourceServiceClient.getResourceData(resourceId));
    }

    @Test
    void testGetResourceDataResourceAccessException() {
        Long resourceId = 3L;

        when(loadBalancedRestTemplate.getForObject(resourceServiceUrl + "/{id}", byte[].class, resourceId))
                .thenThrow(ResourceAccessException.class);

        assertThrows(ResourceAccessException.class, () -> resourceServiceClient.getResourceData(resourceId));
    }

    @Test
    void testRecover() {
        Long resourceId = 4L;
        Exception exception = new ResourceAccessException("Test exception");

        MessageFailedException thrownException = Assertions.assertThrows(
                MessageFailedException.class,
                () -> resourceServiceClient.recover(exception, resourceId)
        );

        assertEquals(
                "Failed to fetch resource data after retries for resourceId: %s".formatted(resourceId),
                thrownException.getMessage()
        );
    }
}