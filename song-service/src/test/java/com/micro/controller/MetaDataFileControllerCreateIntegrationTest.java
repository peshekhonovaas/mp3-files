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
class MetaDataFileControllerCreateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetaDataService metaDataService;

    @BeforeEach
    void setup() {
        reset(metaDataService);
    }

    @Test
    void testCreateSongMetaData() throws Exception {
        when(metaDataService.saveMetaData(any())).thenReturn(1L);

        String jsonPayload = """
        {
            "name": "Test Song",
            "artist": "Test Artist",
            "album": "Test Album",
            "duration": "10:10",
            "year": "2023"
        }
    """;
        mockMvc.perform(post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(metaDataService, times(1)).saveMetaData(any(MetaDataSongDTO.class));
    }

    @Test
    void testCreateSongMetaDataValidationError() throws Exception {
        String invalidPayload = """
        {
            "artist": "Test Artist",
            "album": "Test Album",
            "duration": 210,
            "year": 2022
        }
    """;

        mockMvc.perform(post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest()) // Expect 400 status
                .andExpect(jsonPath("$.errorMessage").value("Validation error")) // Error message
                .andExpect(jsonPath("$.details.duration").value("Duration must be in the format mm:ss")); // Validation error details
    }
}