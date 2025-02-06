package com.micro.service;

import com.micro.dto.FileUploadResponse;
import com.micro.util.MP3Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class MetaDataServiceClientImpl implements MetaDataServiceClient {

    private final RestTemplate restTemplate;
    private final String songServiceUrl;

    @Autowired
    public MetaDataServiceClientImpl(RestTemplateBuilder restTemplateBuilder,
                                     @Value("${song.service.url}") String songServiceUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.songServiceUrl = songServiceUrl;
    }

    public void createSongMetadata(MP3Metadata metadata) {
        restTemplate.postForEntity(songServiceUrl, metadata, FileUploadResponse.class);
    }

    @Override
    public void deleteSongMetadata(List<Long> ids) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(songServiceUrl)
                .queryParam("id", ids);
        restTemplate.delete(builder.toUriString());
    }

    @Override
    public MP3Metadata getSongMetadata(Long id) {
        return restTemplate.getForObject(songServiceUrl + "/{id}", MP3Metadata.class, id);
    }
}
