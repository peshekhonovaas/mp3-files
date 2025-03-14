package com.micro.service;

import com.micro.dto.MetaDataSongDTO;
import com.micro.model.SongMetaData;
import com.micro.repository.SongMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MetaDataServiceImpl implements MetaDataService{
    private final SongMetaDataRepository songMetaDataRepository;

    @Autowired
    public MetaDataServiceImpl(SongMetaDataRepository songMetaDataRepository) {
        this.songMetaDataRepository = songMetaDataRepository;
    }

    @Override
    public long saveMetaData(MetaDataSongDTO metaDataSongDTO) {
        if (Objects.nonNull(metaDataSongDTO.getId()) &&
                this.isMetaDataExists(metaDataSongDTO.getId())) {
            throw new DuplicateKeyException("Metadata for this ID already exists");
        }
        SongMetaData songMetaData = new SongMetaData(metaDataSongDTO.getName(),
                metaDataSongDTO.getArtist(), metaDataSongDTO.getAlbum(),
                metaDataSongDTO.getDuration(), metaDataSongDTO.getYear());
        return this.songMetaDataRepository.save(songMetaData).getId();
    }

    @Override
    public boolean isMetaDataExists(Long resourceId) {
        return this.songMetaDataRepository.existsById(resourceId);
    }

    @Override
    public MetaDataSongDTO getSongMetaDataByResourceId(Long resourceId) {
        SongMetaData songMetaData = this.songMetaDataRepository.findById((resourceId))
                .orElseThrow(()-> new NoSuchElementException("Song metadata with the specified ID does not exist"));
        return new MetaDataSongDTO(songMetaData.getId(), songMetaData.getName(),
                songMetaData.getArtist(), songMetaData.getAlbum(), songMetaData.getDuration(), songMetaData.getYear());
    }

    @Override
    public List<Long> deleteSongMetaDataByResourceIds(String resourceIds) {
        List<Long> listIds = Arrays.stream(resourceIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Long> songMetaDataIds = StreamSupport.stream(this.songMetaDataRepository.findAllById(listIds).spliterator(),
                false).map(SongMetaData::getId).toList();
        if (!songMetaDataIds.isEmpty())
            this.songMetaDataRepository.deleteAllById(songMetaDataIds);
        return songMetaDataIds;
    }
}
