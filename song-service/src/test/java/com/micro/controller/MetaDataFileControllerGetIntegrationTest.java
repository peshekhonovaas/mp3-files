package com.micro.controller;

import com.micro.dto.MetaDataSongDTO;
import com.micro.service.MetaDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MetaDataFileControllerGetIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetaDataService metaDataService;

    @BeforeEach
    void setup() {
        reset(metaDataService);
    }

    @Test
    public void testGetSongMetaData() throws Exception {
        MetaDataSongDTO songDTO = new MetaDataSongDTO(1L, "Test Song", "Test Artist", "Test Album", "210", "2022");
        when(metaDataService.getSongMetaDataByResourceId(1L)).thenReturn(songDTO);

        mockMvc.perform(get("/songs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Response is `200 OK`
                .andExpect(jsonPath("$.id").value(1)) // ID matches
                .andExpect(jsonPath("$.name").value("Test Song"))
                .andExpect(jsonPath("$.artist").value("Test Artist"));

        verify(metaDataService, times(1)).getSongMetaDataByResourceId(1L);
    }
}
