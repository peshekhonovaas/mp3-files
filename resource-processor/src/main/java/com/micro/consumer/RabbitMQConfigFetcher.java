package com.micro.consumer;

import com.micro.exception.ConfigurationFailedException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class RabbitMQConfigFetcher {

    private final RestTemplate restTemplate;
    private volatile Map<String, String> cachedConfig = Collections.emptyMap();
    private final String rabbitMQManagerUrl;

    public RabbitMQConfigFetcher(@Qualifier("nonLoadBalancedRestTemplate") RestTemplate restTemplate,
                                 @Value("${rabbitmq.manager.url}") String rabbitMQManagerUrl) {
        this.restTemplate = restTemplate;
        this.rabbitMQManagerUrl = rabbitMQManagerUrl;
    }

    @Scheduled(fixedRate = 300000)
    public void fetchRabbitMQConfig() {
        try {
            Map<String, String> fetchedConfig = this.restTemplate.exchange(
                    this.rabbitMQManagerUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<Map<String, String>>() {}).getBody();

            if (ObjectUtils.isNotEmpty(fetchedConfig)) {
                this.cachedConfig = Collections.unmodifiableMap(fetchedConfig);
            }
        } catch (Exception ex) {
            throw new ConfigurationFailedException("Failed to fetch RabbitMQ configuration", ex);
        }
    }

    public Map<String, String> getRabbitMQConfig() {
        if (cachedConfig.isEmpty()) this.fetchRabbitMQConfig();
        return this.cachedConfig;
    }
}
