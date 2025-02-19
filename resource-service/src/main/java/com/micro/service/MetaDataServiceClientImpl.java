package com.micro.service;

import com.micro.dto.FileUploadResponse;
import com.micro.util.MP3Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;

@Service
public class MetaDataServiceClientImpl implements MetaDataServiceClient {

    private final RestTemplate restTemplate;
    private final String songServiceUrl;

    @Autowired
    public MetaDataServiceClientImpl(@LoadBalanced RestTemplate restTemplate,
                                     @Value("${song.service.url}") String songServiceUrl) {
        this.restTemplate = restTemplate;
        this.songServiceUrl = songServiceUrl;
    }

    public void createSongMetadata(MP3Metadata metadata) {
        this.restTemplate.postForEntity(this.songServiceUrl, metadata, FileUploadResponse.class);
    }

    @Override
    public void deleteSongMetadata(List<Long> ids) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.songServiceUrl)
                .queryParam("id", ids);
        this.restTemplate.delete(builder.toUriString());
    }

    @Override
    public MP3Metadata getSongMetadata(Long id) {
        return this.restTemplate.getForObject(this.songServiceUrl + "/{id}", MP3Metadata.class, id);
    }
}
