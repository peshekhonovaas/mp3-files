package com.micro.service;

import com.micro.model.AudioFileDetails;
import com.micro.producer.ProducerService;
import com.micro.repository.AudioFileRepository;
import com.micro.uploader.MinioFileStorage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final AudioFileRepository audioFileRepository;
    private final MinioFileStorage minioFileStorage;
    private final ProducerService producerService;

    @Autowired
    public FileStorageServiceImpl(AudioFileRepository audioFileRepository,
                                  MinioFileStorage minioFileStorage,
                                  ProducerService producerService) {
        this.audioFileRepository = audioFileRepository;
        this.minioFileStorage = minioFileStorage;
        this.producerService = producerService;
    }

    @Override
    @Transactional
    public Long uploadFile(String contentType, byte[] content) {
        String random  =  String.valueOf(ThreadLocalRandom.current().nextInt());
        this.minioFileStorage.uploadFile(random, contentType, content);
        Long resourceId = this.audioFileRepository.save(new AudioFileDetails(random)).getId();
        this.producerService.sendResourceId(String.valueOf(resourceId));
        return resourceId;
    }

    @Override
    public byte[] getFile(Long id) {
        AudioFileDetails audioFileDetails = this.audioFileRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Resource with the specified ID does not exist"));
        return this.minioFileStorage.downloadFileAsByteArray(audioFileDetails.getUName());
    }

    @Override
    @Transactional
    public List<Long> deleteFilesAndReturnDeletedIds(String ids) {
        if (ids.isBlank()) return Collections.emptyList();
        List<Long> listIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<AudioFileDetails> audioFileDetails = StreamSupport.stream(this.audioFileRepository.findAllById(listIds).spliterator(),
                        false).toList();
        List<String> fileNames = audioFileDetails.stream().map(AudioFileDetails::getUName).toList();
        List<Long> deletedIds = audioFileDetails.stream().map(AudioFileDetails::getId).toList();
        if (!fileNames.isEmpty()) this.minioFileStorage.removeFiles(fileNames);
        if (!deletedIds.isEmpty()) {
            this.audioFileRepository.deleteAllById(deletedIds);
        }

        return deletedIds;
    }
}
