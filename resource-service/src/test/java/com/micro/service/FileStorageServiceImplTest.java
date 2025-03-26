package com.micro.service;

import com.micro.model.AudioFileDetails;
import com.micro.producer.ProducerService;
import com.micro.repository.AudioFileRepository;
import com.micro.uploader.MinioFileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FileStorageServiceImplTest {

    @Mock
    private AudioFileRepository audioFileRepository;

    @Mock
    private MinioFileStorage minioFileStorage;

    @Mock
    private ProducerService producerService;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUploadFileSuccessfully() {
        String contentType = "audio/mpeg";
        byte[] content = new byte[]{1, 2, 3};

        doNothing().when(minioFileStorage)
                .uploadFile(any(String.class), eq(contentType), eq(content));

        AudioFileDetails mockAudioFileDetails = new AudioFileDetails("mock-file-name") {
            @Override
            public Long getId() {
                return 1L;
            }
        };
        when(audioFileRepository.save(any(AudioFileDetails.class))).thenReturn(mockAudioFileDetails);

        doNothing().when(producerService).sendResourceId("1");
        Long actualResourceId = fileStorageService.uploadFile(contentType, content);

        verify(minioFileStorage, times(1)).uploadFile(any(String.class), eq(contentType), eq(content));
        verify(audioFileRepository, times(1)).save(any(AudioFileDetails.class));
        verify(producerService, times(1)).sendResourceId("1");
        assert actualResourceId.equals(1L);
    }

    @Test
    void shouldFailToUploadWhenStorageFails() {
        String contentType = "audio/mpeg";
        byte[] content = new byte[]{1, 2, 3};
        String expectedErrorMessage = "Storage error";

        doThrow(new RuntimeException(expectedErrorMessage))
                .when(minioFileStorage)
                .uploadFile(any(String.class), eq(contentType), eq(content));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                fileStorageService.uploadFile(contentType, content)
        );

        assert exception.getMessage().equals(expectedErrorMessage);
        verify(minioFileStorage, times(1)).uploadFile(any(String.class), eq(contentType), eq(content));
        verify(audioFileRepository, never()).save(any(AudioFileDetails.class));
        verify(producerService, never()).sendResourceId(any(String.class));
    }

    @Test
    void shouldFailToUploadWhenRepositoryFails() {
        // Arrange
        String contentType = "audio/mpeg";
        byte[] content = new byte[]{1, 2, 3};
        String expectedErrorMessage = "Database error";

        doNothing().when(minioFileStorage).uploadFile(any(String.class), eq(contentType), eq(content));
        when(audioFileRepository.save(any(AudioFileDetails.class))).thenThrow(new RuntimeException(expectedErrorMessage));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                fileStorageService.uploadFile(contentType, content)
        );

        assert exception.getMessage().equals(expectedErrorMessage);
        verify(minioFileStorage, times(1)).uploadFile(any(String.class), eq(contentType), eq(content));
        verify(audioFileRepository, times(1)).save(any(AudioFileDetails.class));
        verify(producerService, never()).sendResourceId(any(String.class));
    }
}