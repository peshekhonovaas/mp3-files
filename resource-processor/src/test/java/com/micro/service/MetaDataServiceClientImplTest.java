package com.micro.service;

import com.micro.dto.CreateMetaDataResponse;
import com.micro.dto.MP3MetadataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class MetaDataServiceClientImplTest {

    @Mock
    private MetaDataServiceClientImpl metaDataServiceClient;

    @Mock
    private RestTemplate loadBalancedRestTemplate;

    @Value("${song.service.url}")
    private String songServiceUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSongMetadataSuccess() {
        MP3MetadataDTO metadataDTO = new MP3MetadataDTO("Song Name", "Artist", "Album", "2023", "3:45");

        doReturn(new CreateMetaDataResponse(1L))
                .when(loadBalancedRestTemplate)
                .postForObject(songServiceUrl, metadataDTO, CreateMetaDataResponse.class);

        assertDoesNotThrow(() -> metaDataServiceClient.createSongMetadata(metadataDTO));
    }
}