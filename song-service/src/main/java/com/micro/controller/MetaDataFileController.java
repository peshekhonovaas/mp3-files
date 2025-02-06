package com.micro.controller;

import com.micro.dto.DeleteResourcesResponse;
import com.micro.dto.MetaDataSongDTO;
import com.micro.service.MetaDataService;
import com.micro.validation.annotation.IdValid;
import com.micro.validation.annotation.IdsLengthValid;
import com.micro.validation.annotation.IdsValid;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/songs")
public class MetaDataFileController {
    private final MetaDataService metaDataService;
    private final static int IDS_LENGTH_RESTRICTION = 200;

    @Autowired
    public MetaDataFileController(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSongMetaData(@Valid @RequestBody MetaDataSongDTO metaDataSongDTO) {
        if (this.metaDataService.isMetaDataExists(metaDataSongDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Metadata for this ID already exists");
        }
        try {
            Long id = this.metaDataService.saveMetaData(metaDataSongDTO);
            return ResponseEntity.ok(id);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("An error occurred on the server: %s", ex.getMessage()), ex);
        }
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSongMetaData(@Valid @IdValid @PathVariable("id") Long id) {
        try {
            MetaDataSongDTO songDTO = this.metaDataService.getSongMetaDataByResourceId(id);
            return ResponseEntity.ok(songDTO);
        } catch (NoSuchElementException ex) {
            throw new EntityNotFoundException(String.format("Song metadata with the specified ID=%d does not exist.", id));
        } catch (Exception ex) {
            throw new RuntimeException(String.format("An error occurred on the server: %s", ex.getMessage()), ex);
        }
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSongs(@Valid @IdsLengthValid(max=IDS_LENGTH_RESTRICTION) @IdsValid @RequestParam("id") String ids) {
        try {
            List<Long> listIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            DeleteResourcesResponse response = new DeleteResourcesResponse(
                    this.metaDataService.deleteSongMetaDataByResourceIds(listIds));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("An error occurred on the server: %s", ex.getMessage()), ex);
        }
    }
}
