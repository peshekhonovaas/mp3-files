package com.micro.parsing;

import com.micro.exception.MetadataParsingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MP3MetaParser {
    private final static Integer STRING_LENGTH = 100;
    private final static Integer FIRST_YEAR = 1900;
    private final static Integer LAST_YEAR = 2099;

    public MP3Metadata parseMetadata(byte[] content){
        try (InputStream input = new ByteArrayInputStream(content)) {
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext parseContext = new ParseContext();
            Mp3Parser mp3Parser = new Mp3Parser();

            mp3Parser.parse(input, handler, metadata, parseContext);

            String name = metadata.get("dc:title");
            String artist = metadata.get("xmpDM:artist");
            String album = metadata.get("xmpDM:album");
            String year = metadata.get("xmpDM:releaseDate");
            String duration = this.formatDuration(metadata.get("xmpDM:duration"));

            if(this.isValidString(name) && this.isValidString(artist) && this.isValidString(album)
                    && this.isValidYear(year) && StringUtils.isNoneEmpty(duration)) {
                return new MP3Metadata(name, artist, album, year, duration);
            }
            throw new IllegalArgumentException("Invalid metadata values in the mp3 file");
        } catch (IOException | TikaException | SAXException ex) {
            throw new MetadataParsingException("Failed to parse content", ex);
        }
    }

    private boolean isValidString(String name) {
        return StringUtils.isNoneEmpty(name) && name.length() <= STRING_LENGTH;
    }

    private boolean isValidYear(String year) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate date = LocalDate.parse(year + "-01-01", formatter);
            return date.getYear() >= FIRST_YEAR && date.getYear() <= LAST_YEAR;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String formatDuration(String duration) {
        if (StringUtils.isEmpty(duration))
            throw new NumberFormatException(String.format("Invalid duration format: %s", duration));
        try {
            double totalSeconds = Double.parseDouble(duration);
            int minutes = (int)(totalSeconds / 60);
            int seconds = (int) (totalSeconds % 60);
            return String.format("%02d:%02d", minutes, seconds);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(String.format("Invalid duration format: %s", ex.getMessage()));
        }
    }
}
