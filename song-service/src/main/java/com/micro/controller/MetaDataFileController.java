package com.micro.controller;

import com.micro.dto.CreateMetaDataResponse;
import com.micro.dto.DeleteMetaDataResponse;
import com.micro.dto.MetaDataSongDTO;
import com.micro.service.MetaDataService;
import com.micro.validation.annotation.IdValid;
import com.micro.validation.annotation.IdsLengthValid;
import com.micro.validation.annotation.IdsValid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CreateMetaDataResponse> createSongMetaData(@Valid @RequestBody MetaDataSongDTO metaDataSongDTO) {
        Long id = this.metaDataService.saveMetaData(metaDataSongDTO);
        CreateMetaDataResponse response = new CreateMetaDataResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MetaDataSongDTO> getSongMetaData(@Valid @IdValid @PathVariable("id") Long id) {
        MetaDataSongDTO songDTO = this.metaDataService.getSongMetaDataByResourceId(id);
        return ResponseEntity.ok(songDTO);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteMetaDataResponse> deleteSongs(@Valid @IdsLengthValid(max=IDS_LENGTH_RESTRICTION) @IdsValid @RequestParam("id") String ids) {
        DeleteMetaDataResponse response = new DeleteMetaDataResponse(
                this.metaDataService.deleteSongMetaDataByResourceIds(ids));
        return ResponseEntity.ok(response);
    }
}
