package com.github.fiecher.turnforge.domain.models;

public class SpecialItem {
    private Long id;
    private String name;
    private String description;
    private String image;

    public SpecialItem() {
    }

    public SpecialItem(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getID() {
        return id;
    }

    public void setID(Long id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
