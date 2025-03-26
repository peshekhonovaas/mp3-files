package com.micro.service;

import com.micro.dto.MetaDataSongDTO;
import com.micro.model.SongMetaData;
import com.micro.repository.SongMetaDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetaDataServiceComponentTest {
    @Autowired
    private MetaDataService metaDataService; // Component under test

    @Autowired
    private SongMetaDataRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testSaveAndRetrieveMetadata() {
        MetaDataSongDTO metadataDTO = new MetaDataSongDTO(null, "Test Song", "Test Artist", "Test Album", "03:30", "2023");

        Long savedId = metaDataService.saveMetaData(metadataDTO);
        Optional<SongMetaData> retrieved = repository.findById(savedId);
        assertTrue(retrieved.isPresent());
        assertEquals("Test Song", retrieved.get().getName());
        assertEquals("2023", retrieved.get().getYear());
    }

    @Test
    void testGetMetadataById() {
        SongMetaData song = new SongMetaData("Test Song", "Test Artist", "Test Album", "03:30", "2023");
        SongMetaData savedSong = repository.save(song);

        MetaDataSongDTO retrievedDTO = metaDataService.getSongMetaDataByResourceId(savedSong.getId());

        assertNotNull(retrievedDTO);
        assertEquals("Test Song", retrievedDTO.getName());
        assertEquals("2023", retrievedDTO.getYear());
    }

    @Test
    void testDeleteMetadataByIds() {
        SongMetaData song1 = repository.save(new SongMetaData("Song 1", "Artist 1", "Album 1", "03:30", "2023"));
        SongMetaData song2 = repository.save(new SongMetaData("Song 2", "Artist 2", "Album 2", "04:00", "2022"));

        String ids = song1.getId() + "," + song2.getId(); // Get comma-separated IDs
        List<Long> deletedIds = metaDataService.deleteSongMetaDataByResourceIds(ids);

        assertEquals(2, deletedIds.size());
        assertFalse(repository.existsById(song1.getId()));
        assertFalse(repository.existsById(song2.getId()));
    }

    @Test
    void testDeleteNonExistentMetadata() {
        List<Long> deletedIds = metaDataService.deleteSongMetaDataByResourceIds("999");

        assertNotNull(deletedIds);
        assertEquals(0, deletedIds.size());
    }
}
