package com.example.marvel_comic_app.model;

public class Hero {
    private String id;
    private String name;
    private String imagenUrl;
    private String publisher;
    private String fullName;
    private String intelligence;
    private String strength;
    private String speed;
    private String power;
    private String combat;

    // Constructor completo
    public Hero(String id, String name, String imagenUrl, String publisher,
                String fullName, String intelligence, String strength,
                String speed, String power, String combat) {
        this.id = id;
        this.name = name;
        this.imagenUrl = imagenUrl;
        this.publisher = publisher;
        this.fullName = fullName;
        this.intelligence = intelligence;
        this.strength = strength;
        this.speed = speed;
        this.power = power;
        this.combat = combat;
    }

    //Contructor para card con nombre e imagen
    public Hero(String id, String name, String imagenUrl) {
        this.id = id;
        this.name = name;
        this.imagenUrl = imagenUrl;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imagenUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIntelligence() {
        return intelligence;
    }

    public String getStrength() {
        return strength;
    }

    public String getSpeed() {
        return speed;
    }

    public String getPower() {
        return power;
    }

    public String getCombat() {
        return combat;
    }

    // Setters
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setIntelligence(String intelligence) {
        this.intelligence = intelligence;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setCombat(String combat) {
        this.combat = combat;
    }
}