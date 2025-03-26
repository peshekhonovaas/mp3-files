package com.micro.consumer;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ResourceService", port = "8080") // Mock Provider setup
public class ResourceProcessorConsumerContractTest {

    @Pact(consumer = "ResourceProcessor", provider = "ResourceService")
    public V4Pact pact(PactDslWithProvider builder) {
        return builder
                .given("Resource with ID 123 exists in Resource Service")
                .uponReceiving("Request for file data with ID 123")
                .method("GET")
                .path("/resources/123")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringType("id", "123")
                        .stringType("content", "mock file data"))
                .toPact(V4Pact.class);
    }

    @Test
    void testGetResourceData() {
        String url = "http://localhost:8080/resources/123";
        RestTemplate restTemplate = new RestTemplate();

        FileData response = restTemplate.getForObject(url, FileData.class);

        assertNotNull(response);
        assertEquals("123", response.getId());
        assertEquals("mock file data", response.getContent());
    }
}

class FileData {
    private String id;
    private String content;

    public String getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
}
