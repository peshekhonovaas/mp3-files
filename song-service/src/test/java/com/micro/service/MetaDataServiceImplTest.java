package com.micro.service;

import com.micro.dto.MetaDataSongDTO;
import com.micro.model.SongMetaData;
import com.micro.repository.SongMetaDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DuplicateKeyException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MetaDataServiceImplTest {

    @Mock
    private SongMetaDataRepository songMetaDataRepository;

    @InjectMocks
    private MetaDataServiceImpl metaDataService;

    @Captor
    private ArgumentCaptor<SongMetaData> songMetaDataCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveMetaDataShouldThrowDuplicateKeyExceptionWhenIDAlreadyExists() {
        MetaDataSongDTO metadataDTO = new MetaDataSongDTO(1L, "Test Song", "Test Artist", "Test Album", "3:45", "2023");

        when(songMetaDataRepository.existsById(1L)).thenReturn(true);
        DuplicateKeyException exception = assertThrows(
                DuplicateKeyException.class,
                () -> metaDataService.saveMetaData(metadataDTO),
                "Expected DuplicateKeyException due to existing ID"
        );

        assertEquals("Metadata for this ID already exists", exception.getMessage());
        verify(songMetaDataRepository, never()).save(any(SongMetaData.class));
    }

    @Test
    void isMetaDataExistsShouldReturnTrueWhenResourceIdExists() {
        Long resourceId = 1L;
        when(songMetaDataRepository.existsById(resourceId)).thenReturn(true);

        boolean exists = metaDataService.isMetaDataExists(resourceId);
        assertTrue(exists);
        verify(songMetaDataRepository, times(1)).existsById(resourceId);
    }

    @Test
    void isMetaDataExistsShouldReturnFalseWhenResourceIdDoesNotExist() {
        Long resourceId = 1L;
        when(songMetaDataRepository.existsById(resourceId)).thenReturn(false);

        boolean exists = metaDataService.isMetaDataExists(resourceId);
        assertFalse(exists);
        verify(songMetaDataRepository, times(1)).existsById(resourceId);
    }

    @Test
    void getSongMetaDataByResourceIdShouldReturnMetadataWhenResourceIdExists() {
        Long resourceId = 1L;
        SongMetaData songMetaData = new SongMetaData("Test Song", "Test Artist", "Test Album", "3:45", "2023");
        when(songMetaDataRepository.findById(resourceId)).thenReturn(Optional.of(songMetaData));

        MetaDataSongDTO result = metaDataService.getSongMetaDataByResourceId(resourceId);
        assertNotNull(result);
        assertEquals("Test Song", result.getName());
        assertEquals("Test Artist", result.getArtist());
        assertEquals("Test Album", result.getAlbum());
        assertEquals("3:45", result.getDuration());
        assertEquals("2023", result.getYear());
    }

    @Test
    void getSongMetaDataByResourceIdShouldThrowNoSuchElementExceptionWhenResourceIdDoesNotExist() {
        Long resourceId = 1L;
        when(songMetaDataRepository.findById(resourceId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> metaDataService.getSongMetaDataByResourceId(resourceId)
        );

        assertEquals("Song metadata with the specified ID does not exist", exception.getMessage());
        verify(songMetaDataRepository, times(1)).findById(resourceId);
    }

    @Test
    void deleteSongMetaDataByResourceIdsShouldReturnEmptyListWhenNoMatchingIds() {
        String resourceIds = "1,2,3";
        List<Long> parsedIds = List.of(1L, 2L, 3L);

        when(songMetaDataRepository.findAllById(parsedIds)).thenReturn(Collections.emptyList());
        List<Long> deletedIds = metaDataService.deleteSongMetaDataByResourceIds(resourceIds);

        assertNotNull(deletedIds);
        assertEquals(0, deletedIds.size());
        verify(songMetaDataRepository, times(1)).findAllById(parsedIds);
        verify(songMetaDataRepository, never()).deleteAllById(anyList());
    }
}