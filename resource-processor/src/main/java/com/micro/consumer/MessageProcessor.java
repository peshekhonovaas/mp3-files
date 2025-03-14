package com.micro.consumer;

import com.micro.dto.MP3MetadataDTO;
import com.micro.exception.MessageFailedException;
import com.micro.parsing.MP3MetaParser;
import com.micro.service.MetaDataServiceClient;
import com.micro.service.ResourceServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class MessageProcessor {
    private final ResourceServiceClient resourceServiceClient;
    private final MetaDataServiceClient metaDataServiceClient;

    public MessageProcessor(ResourceServiceClient resourceServiceClient,
                            MetaDataServiceClient metaDataServiceClient) {
        this.resourceServiceClient = resourceServiceClient;
        this.metaDataServiceClient = metaDataServiceClient;
    }

    @Bean
    public Consumer<Message<String>> processMetadata() {
        return this::handleMessage;
    }

    @Retryable(
            retryFor = { MessageFailedException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    public void handleMessage(Message<String> message) throws RuntimeException {
        Optional.of(message.getPayload()).ifPresent(resourceId -> {
            byte[] content = this.resourceServiceClient.getResourceData(Long.valueOf(resourceId));

           MP3MetaParser parser = new MP3MetaParser();
           MP3MetadataDTO metadata = parser.parseMetadata(content);

          this.metaDataServiceClient.createSongMetadata(metadata);
        });
    }

    @Recover
    public void recover(MessageFailedException exception, Message<Map<String, Object>> message) {
        throw new MessageFailedException(String.format("Failed to process message resource Id %s after retries",
                message.getPayload().get("resourceId")), exception);
    }
}
