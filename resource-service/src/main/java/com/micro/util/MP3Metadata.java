package com.micro.util;

public class MP3Metadata {
    private Long id;
    private final String name;
    private final String artist;
    private final String album;
    private final String year;
    private final String duration;

    public MP3Metadata(String name, String artist, String album, String year, String duration) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.duration = duration;
    }
    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    public String getDuration() {
        return duration;
    }
}