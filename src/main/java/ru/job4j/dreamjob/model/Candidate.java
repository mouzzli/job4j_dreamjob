package ru.job4j.dreamjob.model;

import ru.job4j.dreamjob.util.Util;

import java.time.LocalDateTime;

public class Candidate {
    private int id;
    private String name;
    private String description;
    private LocalDateTime created;

    public Candidate() {
    }

    public Candidate(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = LocalDateTime.now();
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

    public String getCreated() {
        return created.format(Util.getFormatter());
    }
}