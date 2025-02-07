package com.micro.service;

import java.util.List;

public interface FileStorageService {
    Long uploadFile(String contentType, byte[] content);
    byte[] getFile(Long id);
    List<Long> deleteFilesAndReturnDeletedIds(String ids);
}
