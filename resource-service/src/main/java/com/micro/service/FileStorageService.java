package com.micro.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    Long uploadFile(String contentType, byte[] content) throws Exception;
    byte[] getFile(Long id);
    List<Long> deleteFilesAndReturnDeletedIds(List<Long> ids);
}
