package com.micro.service;

import com.micro.dto.MetaDataSongDTO;

import java.util.List;

public interface MetaDataService {
    long saveMetaData(MetaDataSongDTO metaDataSongDTO);
    MetaDataSongDTO getSongMetaDataByResourceId(Long resourceId);
    List<Long> deleteSongMetaDataByResourceIds(String resourceIds);
    boolean isMetaDataExists(Long resourceId);
}
