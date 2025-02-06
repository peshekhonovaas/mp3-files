package com.micro.service;

import com.micro.model.AudioFileDetails;
import com.micro.repository.AudioFileRepository;
import com.micro.uploader.MinioFileStorage;
import com.micro.util.MP3MetaParser;
import com.micro.util.MP3Metadata;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final AudioFileRepository audioFileRepository;
    private final MinioFileStorage minioFileStorage;
    private final MetaDataServiceClient metaDataServiceClient;

    @Autowired
    public FileStorageServiceImpl(AudioFileRepository audioFileRepository,
                                  MinioFileStorage minioFileStorage,
                                  MetaDataServiceClientImpl songServiceClient) {
        this.audioFileRepository = audioFileRepository;
        this.minioFileStorage = minioFileStorage;
        this.metaDataServiceClient = songServiceClient;
    }

    @Override
    @Transactional
    public Long uploadFile(String contentType, byte[] content) throws Exception {

        MP3MetaParser parser = new MP3MetaParser();
        MP3Metadata metadata = parser.parseMetadata(content);

        this.minioFileStorage.uploadFile(metadata.getName(), contentType, content);
        Long id = this.audioFileRepository.save(new AudioFileDetails(metadata.getName())).getId();
        metadata.setId(id);
        this.metaDataServiceClient.createSongMetadata(metadata);
        return id;
    }

    @Override
    public byte[] getFile(Long id) {
        AudioFileDetails audioFileDetails = this.audioFileRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Resource with the specified ID does not exist"));
        return this.minioFileStorage.downloadFileAsByteArray(audioFileDetails.getUName());
    }

    @Override
    @Transactional
    public List<Long> deleteFilesAndReturnDeletedIds(List<Long> ids) {
        List<AudioFileDetails> audioFileDetails = StreamSupport.stream(this.audioFileRepository.findAllById(ids).spliterator(),
                        false).toList();
        List<String> fileNames = audioFileDetails.stream().map(AudioFileDetails::getUName).toList();
        List<Long> deletedIds = audioFileDetails.stream().map(AudioFileDetails::getId).toList();
        if (!fileNames.isEmpty()) this.minioFileStorage.removeFiles(fileNames);
        if (!deletedIds.isEmpty()) {
            this.audioFileRepository.deleteAllById(deletedIds);
            this.metaDataServiceClient.deleteSongMetadata(deletedIds);
        }
        return deletedIds;
    }
}
