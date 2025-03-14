package com.micro.service;

import com.micro.exception.MessageFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class ResourceServiceClientImpl implements ResourceServiceClient {

    private final RestTemplate loadBalancedRestTemplate;
    private final String resourceServiceUrl;

    @Autowired
    public ResourceServiceClientImpl(@LoadBalanced @Qualifier("loadBalancedRestTemplate") RestTemplate loadBalancedRestTemplate,
                                     @Value("${resource.service.url}") String resourceServiceUrl) {
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.resourceServiceUrl = resourceServiceUrl;
    }

    @Override
    @Retryable(
            retryFor = { HttpServerErrorException.class, ResourceAccessException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    public byte[] getResourceData(Long resourceId) {
        return this.loadBalancedRestTemplate.getForObject(this.resourceServiceUrl + "/{id}", byte[].class, resourceId);
    }

    @Recover
    public byte[] recover(Exception ex, Long resourceId) {
        throw new MessageFailedException(
                "Failed to fetch resource data after retries for resourceId: %s".formatted(resourceId), ex);
    }
}
