package com.micro.parsing;

import com.micro.dto.MP3MetadataDTO;
import com.micro.exception.MetadataParsingException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MP3MetaParserTest {
    private MP3MetaParser parser;
    private Mp3Parser mockMp3Parser;

    @BeforeEach
    void setUp() {
        mockMp3Parser = mock(Mp3Parser.class);
        this.parser = new MP3MetaParser(mockMp3Parser);
    }

    @Test
    void parseMetadataShouldReturnMetadataWhenValidContentProvided() throws TikaException, IOException, SAXException {
        byte[] content = "mock mp3 content".getBytes();

        doAnswer(invocation -> {
            // Simulates the behavior of `Mp3Parser.parse`
            Object[] args = invocation.getArguments();
            Metadata metadataArg = (Metadata) args[2]; // Pass mock metadata
            metadataArg.add("dc:title", "Test Title");
            metadataArg.add("xmpDM:artist","Test Artist");
            metadataArg.add("xmpDM:album","Test Album");
            metadataArg.add("xmpDM:releaseDate", "2023");
            metadataArg.add("xmpDM:duration", "300");
            return null;
        }).when(mockMp3Parser).parse(any(ByteArrayInputStream.class), any(), any(), any());

        MP3MetadataDTO result = this.parser.parseMetadata(content);

        assertNotNull(result);
            assertEquals("Test Title", result.name());
            assertEquals("Test Artist", result.artist());
            assertEquals("Test Album", result.album());
            assertEquals("2023", result.year());
            assertEquals("05:00", result.duration());
    }

    @Test
    void parseMetadataShouldThrowIllegalArgumentExceptionWhenInvalidMetadataValues() {
        byte[] content = "mockMp3Content".getBytes();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseMetadata(content),
                "Expected IllegalArgumentException for invalid metadata values."
        );

        assertEquals("Invalid duration format: null", exception.getMessage());
    }

    @Test
    void parseMetadataShouldThrowMetadataParsingExceptionWhenIOExceptionOccurs()
            throws TikaException, IOException, SAXException {
        byte[] content = "mockMp3Content".getBytes();

        doThrow(new IOException("Simulated IOException"))
                .when(mockMp3Parser)
                .parse(any(ByteArrayInputStream.class), any(), any(), any());

        assertThrows(
                MetadataParsingException.class,
                () -> parser.parseMetadata(content),
                "Expected MetadataParsingException when IOException occurs."
        );
    }

    private static byte[] convertMetadataToByteArray(Metadata metadata) throws Exception {
        StringBuilder builder = new StringBuilder();

        for (String name : metadata.names()) {
            if (metadata.get(name) != null) {
                builder.append(name)
                        .append("=")
                        .append(metadata.get(name))
                        .append("\n");
            }
        }
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }
}