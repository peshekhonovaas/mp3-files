package com.micro.controller;

import com.micro.dto.DeleteResourcesResponse;
import com.micro.dto.FileUploadResponse;
import com.micro.service.FileStorageService;
import com.micro.validation.annotation.ContentTypeValid;
import com.micro.validation.annotation.IdValid;
import com.micro.validation.annotation.IdsLengthValid;
import com.micro.validation.annotation.IdsValid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
public class MP3FileController {
    private final static int IDS_LENGTH_RESTRICTION = 200;
    private final FileStorageService fileStorageService;

    @Autowired
    public MP3FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(@Valid @ContentTypeValid @RequestHeader("Content-Type") String contentType,
                                        @RequestBody byte[] fileContent){
        Long id = fileStorageService.uploadFile(contentType, fileContent);
        FileUploadResponse response = new FileUploadResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<byte[]> getFile(@Valid @IdValid @PathVariable("id") Long id) {
        byte[] data = fileStorageService.getFile(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.setContentLength(data.length);
        return ResponseEntity.ok().headers(headers).body(data);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteResourcesResponse> deleteResources(@Valid @IdsLengthValid(max = IDS_LENGTH_RESTRICTION) @IdsValid @RequestParam("id") String ids) {
        DeleteResourcesResponse response = new DeleteResourcesResponse(
                this.fileStorageService.deleteFilesAndReturnDeletedIds(ids));
        return ResponseEntity.ok(response);
    }
}
