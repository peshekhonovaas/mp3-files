package com.micro.util;

public class MP3Metadata {
    private String name;
    private String artist;
    private String album;
    private String year;
    private String duration;
    private Long id;

    public MP3Metadata(String name, String artist, String album, String year, String duration) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.duration = duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getYear() {
        return year;
    }

    public String getDuration() {
        return duration;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}