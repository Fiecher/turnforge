package com.github.fiecher.turnforge.domain.models;

import java.util.Objects;

public class Trait {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String prerequisites;
    private String type;

    public Trait(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Trait(Long id, String name, String description, String image, String prerequisites, String type) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.image = image;
        this.prerequisites = prerequisites;
        this.type = type;
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

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || id == null) return false;
        Trait trait = (Trait) o;
        return Objects.equals(id, trait.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}