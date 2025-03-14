package com.micro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MetaDataSongDTO {
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Artist is required")
    @Size(min = 1, max = 100, message = "Artist must be between 1 and 100 characters")
    private String artist;

    @NotNull(message = "Album is required")
    @Size(min = 1, max = 100, message = "Album must be between 1 and 100 characters")
    private String album;

    @NotNull(message = "Duration is required")
    @Pattern(regexp = "^([0-5]?[0-9]):([0-5]?[0-9])$", message = "Duration must be in the format mm:ss")
    private String duration;

    @NotNull(message = "Year is required")
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Year must be in the format YYYY between 1900 and 2099")
    private String year;

    public MetaDataSongDTO() {
    }

    public MetaDataSongDTO(Long id, String name, String artist, String album, String duration, String year) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    public String getYear() {
        return year;
    }
}
