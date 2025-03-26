package com.micro.consumer;

import com.micro.exception.MessageFailedException;
import com.micro.service.MetaDataServiceClient;
import com.micro.service.ResourceServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MessageProcessorTest {
    @Test
    void handleMessageThrowsExceptionRetriesAndFails() {
        ResourceServiceClient resourceServiceClient = mock(ResourceServiceClient.class);
        MetaDataServiceClient metaDataServiceClient = mock(MetaDataServiceClient.class);
        MessageProcessor messageProcessor = new MessageProcessor(resourceServiceClient, metaDataServiceClient);

        String resourceId = "123";
        Message<String> message = MessageBuilder.withPayload(resourceId).build();

        when(resourceServiceClient.getResourceData(Long.valueOf(resourceId)))
                .thenThrow(new RuntimeException("Connection error"));
        assertThrows(RuntimeException.class, () -> messageProcessor.handleMessage(message));
        verifyNoInteractions(metaDataServiceClient);
    }

    @Test
    void recoverWhenRetriesFailThrowsMessageFailedException() {
        ResourceServiceClient resourceServiceClient = mock(ResourceServiceClient.class);
        MetaDataServiceClient metaDataServiceClient = mock(MetaDataServiceClient.class);
        MessageProcessor messageProcessor = new MessageProcessor(resourceServiceClient, metaDataServiceClient);

        Message<Map<String, String>> failedMessage = MessageBuilder.withPayload(Map.of("resourceId", "123")).build();
        MessageFailedException exception = new MessageFailedException("Test failure", new RuntimeException("Test exception"));

        MessageFailedException thrownException = assertThrows(MessageFailedException.class,
                () -> messageProcessor.recover(exception, failedMessage));

        assertEquals("Failed to process message resource Id 123 after retries", thrownException.getMessage());
        assertEquals(exception, thrownException.getCause());
    }
}