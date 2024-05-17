package com.example.taskpad;

public class Task {
    private int id;
    private String title;
    private String description;

    // Constructor for new tasks (id will be auto-incremented by the database)
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Constructor for tasks retrieved from the database
    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
