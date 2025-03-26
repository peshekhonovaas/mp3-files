package com.micro.controller;

import com.micro.service.MetaDataService;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MetaDataFileControllerDeleteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetaDataService metaDataService;

    @BeforeEach
    void setup() {
        reset(metaDataService);
    }

    @Test
    public void testDeleteSongMetaData() throws Exception {
        when(metaDataService.deleteSongMetaDataByResourceIds("1,2,3")).thenReturn(List.of(1L, 2L, 3L)); // Service mock returns 3 indicating 3 deletions

        mockMvc.perform(delete("/songs")
                        .queryParam("id", "1,2,3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(metaDataService, times(1)).deleteSongMetaDataByResourceIds("1,2,3");
    }

    @Test
    public void testDeleteSongMetaDataValidationError() throws Exception {
        mockMvc.perform(delete("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("id", ""))
                .andExpect(status().isBadRequest());
    }
}