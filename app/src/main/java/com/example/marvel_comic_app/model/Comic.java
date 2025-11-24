package com.example.marvel_comic_app.model;

public class Comic {
    private String id;
    private String title;
    private String imagenUrl;
    private double price;
    private boolean selected;

    public Comic(String id, String title, String imagenUrl, double price) {
        this.id = id;
        this.title = title;
        this.imagenUrl = imagenUrl;
        this.price = price;
        this.selected = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public double getPrice() {
        return price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}