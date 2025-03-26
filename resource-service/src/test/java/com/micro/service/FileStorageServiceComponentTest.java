package com.micro.service;

import com.micro.model.AudioFileDetails;
import com.micro.producer.ProducerService;
import com.micro.repository.AudioFileRepository;
import com.micro.uploader.MinioFileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileStorageServiceComponentTest {

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @MockitoBean
    private AudioFileRepository audioFileRepository;

    @MockitoBean
    private MinioFileStorage minioFileStorage;

    @MockitoBean
    private ProducerService producerService;

    @BeforeEach
    void setup() {
        reset(audioFileRepository, minioFileStorage, producerService);
    }

    @Test
    void testUploadFileSuccess() {
        String contentType = "audio/mpeg";
        byte[] content = "mock file content".getBytes();
        String generatedFileName = "random1234";
        AudioFileDetails savedAudioFile = new AudioFileDetails(generatedFileName) {
            @Override
            public Long getId() {
                return 1L;
            }
        };

        doNothing().when(minioFileStorage).uploadFile(eq(generatedFileName), eq(contentType), eq(content));
        when(audioFileRepository.save(any(AudioFileDetails.class))).thenReturn(savedAudioFile);
        doNothing().when(producerService).sendResourceId("1");

        Long resourceId = fileStorageService.uploadFile(contentType, content);

        verify(minioFileStorage, times(1)).uploadFile(any(), eq(contentType), eq(content));
        verify(audioFileRepository, times(1)).save(any(AudioFileDetails.class));
        verify(producerService, times(1)).sendResourceId("1");
    }

    @Test
    void testGetFileSuccess() {
        Long fileId = 1L;
        AudioFileDetails audioFileDetails = new AudioFileDetails("random1234");
        byte[] mockFileData = "mock file content".getBytes();

        when(audioFileRepository.findById(fileId)).thenReturn(Optional.of(audioFileDetails));
        when(minioFileStorage.downloadFileAsByteArray("random1234")).thenReturn(mockFileData);

        byte[] retrievedFile = fileStorageService.getFile(fileId);

        assertNotNull(retrievedFile);
        assertArrayEquals(mockFileData, retrievedFile);
        verify(audioFileRepository, times(1)).findById(fileId);
        verify(minioFileStorage, times(1)).downloadFileAsByteArray("random1234");
    }

    @Test
    void testGetFileResourceNotFound() {
        Long fileId = 1L;
        when(audioFileRepository.findById(fileId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> fileStorageService.getFile(fileId),
                "Expected NoSuchElementException"
        );

        assertEquals("Resource with the specified ID does not exist", exception.getMessage());
        verify(audioFileRepository, times(1)).findById(fileId);
        verify(minioFileStorage, never()).downloadFileAsByteArray(anyString());
    }

    @Test
    void testDeleteFilesAndReturnDeletedIdsSuccess() {
        String ids = "1,2,3";
        List<Long> listIds = Arrays.asList(1L, 2L, 3L);
        List<AudioFileDetails> mockAudioFiles = listIds.stream()
                .map(id -> {
                    AudioFileDetails audioFile = new AudioFileDetails("file" + id);
                    return audioFile;
                }).collect(Collectors.toList());

        when(audioFileRepository.findAllById(listIds)).thenReturn(mockAudioFiles);
        doNothing().when(minioFileStorage).removeFiles(anyList());
        doNothing().when(audioFileRepository).deleteAllById(anyList());

        List<Long> deletedIds = fileStorageService.deleteFilesAndReturnDeletedIds(ids);

        assertEquals(3, deletedIds.size());
        verify(audioFileRepository, times(1)).findAllById(listIds);
        verify(minioFileStorage, times(1)).removeFiles(anyList());
    }

    @Test
    void testDeleteFilesAndReturnDeletedIdsEmptyIds() {
        List<Long> deletedIds = fileStorageService.deleteFilesAndReturnDeletedIds("");

        assertNotNull(deletedIds);
        assertTrue(deletedIds.isEmpty());
        verify(audioFileRepository, never()).findAllById(anyList());
        verify(minioFileStorage, never()).removeFiles(anyList());
    }

    @Test
    void testDeleteFilesAndReturnDeletedIdsNoMatchingFiles() {
        String ids = "4,5,6";
        List<Long> listIds = Arrays.asList(4L, 5L, 6L);

        when(audioFileRepository.findAllById(listIds)).thenReturn(Collections.emptyList());

        List<Long> deletedIds = fileStorageService.deleteFilesAndReturnDeletedIds(ids);

        assertNotNull(deletedIds);
        assertTrue(deletedIds.isEmpty());
        verify(audioFileRepository, times(1)).findAllById(listIds);
        verify(minioFileStorage, never()).removeFiles(anyList());
        verify(audioFileRepository, never()).deleteAllById(anyList());
    }
}
