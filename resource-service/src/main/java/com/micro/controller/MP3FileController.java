package com.micro.controller;

import com.micro.dto.DeleteResourcesResponse;
import com.micro.dto.FileUploadResponse;
import com.micro.service.FileStorageService;
import com.micro.validation.annotation.ContentTypeValid;
import com.micro.validation.annotation.IdValid;
import com.micro.validation.annotation.IdsLengthValid;
import com.micro.validation.annotation.IdsValid;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> uploadFile(@Valid @ContentTypeValid @RequestHeader("Content-Type") String contentType,
                                        @RequestBody byte[] fileContent){
       try {
            Long id = fileStorageService.uploadFile(contentType, fileContent);
            FileUploadResponse response = new FileUploadResponse(id);
           return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("An error occurred on the server: %s", ex.getMessage()), ex);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getFile(@Valid @IdValid @PathVariable("id") Long id) {
       try {
            byte[] data = fileStorageService.getFile(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
            headers.setContentLength(data.length);
            return ResponseEntity.ok().headers(headers).body(data);
        } catch (NoSuchElementException ex) {
            throw new EntityNotFoundException(String.format("Resource with ID=%d not found", id));
        } catch (Exception ex) {
            throw new RuntimeException(String.format("An error occurred on the server: %s", ex.getMessage()), ex);
        }
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteResources(@Valid @IdsLengthValid(max = IDS_LENGTH_RESTRICTION) @IdsValid @RequestParam("id") String ids) {
        try {
            List<Long> listIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            DeleteResourcesResponse response = new DeleteResourcesResponse(
                    this.fileStorageService.deleteFilesAndReturnDeletedIds(listIds));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("An error occurred on the server: %s", ex.getMessage()), ex);
        }
    }
}
