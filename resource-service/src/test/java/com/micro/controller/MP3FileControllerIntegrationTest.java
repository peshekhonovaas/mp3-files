package com.micro.controller;

import com.micro.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MP3FileControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileStorageService fileStorageService;

    @BeforeEach
    void setup() {
        reset(fileStorageService);
    }

    @Test
    void testUploadFileInvalidContentType() throws Exception {
        byte[] fileContent = "mock file content".getBytes();

        mockMvc.perform(post("/resources")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header("Content-Type", "text/plain")
                        .content(fileContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Invalid file type"));

        verify(fileStorageService, times(0)).uploadFile(anyString(), any(byte[].class));
    }

    @Test
    void testGetFileSuccess() throws Exception {
        byte[] mockFileData = "mock file content".getBytes();
        when(fileStorageService.getFile(100L)).thenReturn(mockFileData);

        mockMvc.perform(get("/resources/{id}", 100L))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "audio/mpeg"))
                .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, mockFileData.length))
                .andExpect(content().bytes(mockFileData));

        verify(fileStorageService, times(1)).getFile(100L);
    }

    @Test
    void testDeleteResourcesSuccess() throws Exception {
        when(fileStorageService.deleteFilesAndReturnDeletedIds("1,2,3")).thenReturn(List.of(1L, 2L, 3L));

        mockMvc.perform(delete("/resources")
                        .queryParam("id", "1,2,3")) // Valid IDs
                .andExpect(status().isOk());

        verify(fileStorageService, times(1)).deleteFilesAndReturnDeletedIds("1,2,3");
    }

    @Test
    void testDeleteResourcesInvalidIdFormat() throws Exception {
        mockMvc.perform(delete("/resources")
                        .queryParam("id", "id1,id2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("The provided IDs are invalid"));
    }

    @Test
    void testDeleteResourcesIdsExceedLimit() throws Exception {
        String excessiveIds = String.join(",", Collections.nCopies(201, "1"));

        mockMvc.perform(delete("/resources")
                        .queryParam("id", excessiveIds))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("ID list exceeds length restrictions"));
    }
}
