package com.micro.service;

import com.micro.dto.MP3MetadataDTO;
import com.micro.parsing.MP3MetaParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {ResourceProcessorServiceImpl.class, MetaDataServiceClient.class, ResourceServiceClient.class})
public class ResourceProcessorComponentTest {
    @Autowired
    private ResourceProcessorServiceImpl resourceProcessorService;

    @MockitoBean
    private ResourceServiceClient resourceServiceClient;

    @MockitoBean
    private MetaDataServiceClient metaDataServiceClient;

    @BeforeEach
    void setup() {
        reset(resourceServiceClient, metaDataServiceClient);
    }

    @Test
    void testSaveMetaDataResourceNotFound() {
        Long resourceId = 999L;

        when(resourceServiceClient.getResourceData(resourceId))
                .thenThrow(new NoSuchElementException("Resource not found"));

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> resourceProcessorService.saveMetaDataData(resourceId),
                "Expected exception for missing resource"
        );

        assertEquals("Resource not found", exception.getMessage());

        verify(resourceServiceClient, times(1)).getResourceData(resourceId);
        verify(metaDataServiceClient, never()).createSongMetadata(any(MP3MetadataDTO.class));
    }

    @Test
    void testSaveMetaDataInvalidMetadataParsing() {
        Long resourceId = 456L;
        byte[] invalidResourceData = "invalid file data".getBytes();

        when(resourceServiceClient.getResourceData(resourceId)).thenReturn(invalidResourceData);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> resourceProcessorService.saveMetaDataData(resourceId),
                "Expected exception for invalid MP3 metadata parsing"
        );

        assertTrue(exception.getMessage().contains("Invalid duration format"));
        verify(resourceServiceClient, times(1)).getResourceData(resourceId);
        verify(metaDataServiceClient, never()).createSongMetadata(any(MP3MetadataDTO.class));
    }
}
