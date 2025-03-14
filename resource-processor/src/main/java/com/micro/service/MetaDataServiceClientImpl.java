package com.micro.service;

import com.micro.dto.CreateMetaDataResponse;
import com.micro.dto.MP3MetadataDTO;
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
public class MetaDataServiceClientImpl implements MetaDataServiceClient {

    private final RestTemplate loadBalancedRestTemplate;
    private final String songServiceUrl;

    @Autowired
    public MetaDataServiceClientImpl(@LoadBalanced @Qualifier("loadBalancedRestTemplate") RestTemplate loadBalancedRestTemplate,
                                     @Value("${song.service.url}") String songServiceUrl) {
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.songServiceUrl = songServiceUrl;
    }

    @Override
    @Retryable(
            retryFor = { HttpServerErrorException.class, ResourceAccessException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    public void createSongMetadata(MP3MetadataDTO metadata) {
        this.loadBalancedRestTemplate.postForObject(this.songServiceUrl, metadata, CreateMetaDataResponse.class);
    }

    @Recover
    public void recover(Exception ex, MP3MetadataDTO metadata) {
        throw new MessageFailedException(
                "Failed to send metadata after retries: %s".formatted(metadata.name()), ex);
    }
}
