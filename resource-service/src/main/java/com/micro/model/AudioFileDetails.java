package com.micro.model;

import jakarta.persistence.*;

@Entity
public class AudioFileDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String name;

    public AudioFileDetails(String name) {
        this.name = name;
    }

    public AudioFileDetails() {
    }
    public Long getId() {
        return id;
    }
    public String getUName() {
        return name;
    }
}
