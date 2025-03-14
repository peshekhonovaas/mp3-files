package com.micro.service;

import com.micro.dto.MP3MetadataDTO;
import com.micro.parsing.MP3MetaParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceProcessorServiceImpl implements ResourceProcessorService {
    private final MetaDataServiceClient metaDataServiceClient;
    private final ResourceServiceClient resourceServiceClient;

    @Autowired
    public ResourceProcessorServiceImpl(MetaDataServiceClient metaDataServiceClient,
                                        ResourceServiceClient resourceServiceClient) {
        this.metaDataServiceClient = metaDataServiceClient;
        this.resourceServiceClient = resourceServiceClient;
    }
    @Override
    public void saveMetaDataData(Long resourceId) {
        byte[] data = this.resourceServiceClient.getResourceData(resourceId);
        MP3MetaParser parser = new MP3MetaParser();
        MP3MetadataDTO metaData = parser.parseMetadata(data);
        this.metaDataServiceClient.createSongMetadata(metaData);
    }
}
