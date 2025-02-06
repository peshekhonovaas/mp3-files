package com.micro.dto;

public class FileUploadResponse {
    private Long id;

    public FileUploadResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}