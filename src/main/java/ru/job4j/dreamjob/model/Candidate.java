package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;

public class Candidate {
    private int id;
    private String name;
    private String description;
    private LocalDateTime created;
    private byte[] photo;

    private City city;

    public Candidate() {
    }

    public Candidate(int id, String name, String description, City city) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = LocalDateTime.now();
        this.city = city;
    }

    public Candidate(int id, String name, String description, City city, byte[] photo, LocalDateTime localDateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = localDateTime;
        this.city = city;
        this.photo = photo;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
