package com.micro.service;

import com.micro.util.MP3Metadata;

import java.util.List;

public interface MetaDataServiceClient {
    Long createSongMetadata(MP3Metadata metadata);
    void deleteSongMetadata(List<Long> ids);
    MP3Metadata getSongMetadata(Long id);
}
