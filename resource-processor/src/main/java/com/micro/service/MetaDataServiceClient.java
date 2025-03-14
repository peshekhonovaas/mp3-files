package com.micro.service;

import com.micro.dto.MP3MetadataDTO;

public interface MetaDataServiceClient {
    void createSongMetadata(MP3MetadataDTO metadata);
}