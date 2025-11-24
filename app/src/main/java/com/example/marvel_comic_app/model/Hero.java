package com.example.marvel_comic_app.model;

public class Hero {
    private String id;
    private String name;
    private String imagenUrl;

    public Hero(String id, String name, String imagenUrl) {
        this.id = id;
        this.name = name;
        this.imagenUrl = imagenUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imagenUrl;
    }
}