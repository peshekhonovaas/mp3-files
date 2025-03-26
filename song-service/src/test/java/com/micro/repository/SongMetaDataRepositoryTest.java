package com.micro.repository;

import com.micro.model.SongMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SongMetaDataRepositoryTest {

    @Autowired
    private SongMetaDataRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        SongMetaData song = new SongMetaData("Test Song", "Test Artist", "Test Album", "180", "2022");
        SongMetaData savedSong = repository.save(song);

        Optional<SongMetaData> retrievedSong = repository.findById(savedSong.getId());

        assertTrue(retrievedSong.isPresent());
        assertEquals(savedSong.getName(), retrievedSong.get().getName());
        assertEquals("Test Artist", retrievedSong.get().getArtist());
    }

    @Test
    void testDeleteById() {
        SongMetaData song = new SongMetaData("Test Song", "Test Artist", "Test Album", "180", "2022");
        SongMetaData savedSong = repository.save(song);

        repository.deleteById(savedSong.getId());
        Optional<SongMetaData> retrievedSong = repository.findById(savedSong.getId());
        assertFalse(retrievedSong.isPresent());
    }
}