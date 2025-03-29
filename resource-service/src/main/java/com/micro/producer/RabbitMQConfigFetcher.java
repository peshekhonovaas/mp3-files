package com.micro.producer;

import com.micro.exception.ConfigurationFailedException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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
    private final String rabbitMQManagerUrl;
    private volatile Map<String, String> cachedConfig = Collections.emptyMap();

    @Autowired
    public RabbitMQConfigFetcher(@LoadBalanced RestTemplate restTemplate,
                                 @Value("${rabbitmq.manager.url}") String rabbitMQManagerUrl) {
        this.restTemplate = restTemplate;
        this.rabbitMQManagerUrl = rabbitMQManagerUrl;
    }

    @Scheduled(fixedRate = 300000)
    public void fetchAndCacheConfig() {
        try {
            Map<String, String> fetchedConfig = this.restTemplate.exchange(
                    this.rabbitMQManagerUrl + "/rabbitmq/config", HttpMethod.GET, null,
                    new ParameterizedTypeReference<Map<String, String>>() {}).getBody();

            if (ObjectUtils.isNotEmpty(fetchedConfig)) {
                this.cachedConfig = Collections.unmodifiableMap(fetchedConfig);
            }
        } catch (Exception ex) {
            throw new ConfigurationFailedException("Failed to fetch RabbitMQ configuration", ex);
        }
    }

    public Map<String, String> getConfig() {
        if (this.cachedConfig.isEmpty()) this.fetchAndCacheConfig();
        return this.cachedConfig;
    }
}
