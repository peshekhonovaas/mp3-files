package com.micro.service;

import com.micro.dto.MetaDataSongDTO;

import java.util.List;

public interface MetaDataService {
    long saveMetaData(MetaDataSongDTO metaDataSongDTO);
    MetaDataSongDTO getSongMetaDataByResourceId(Long resourceId);
    List<Long> deleteSongMetaDataByResourceIds(List<Long> resourceIds);
    boolean isMetaDataExists(Long resourceId);
}
