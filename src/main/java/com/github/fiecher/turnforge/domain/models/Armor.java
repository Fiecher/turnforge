package com.github.fiecher.turnforge.domain.models;

import java.util.Objects;

public class Armor {
    private Long id;
    private String name;
    private String description;
    private String image;
    private short ac;
    private String type;
    private Double weight;
    private Integer price;

    public Armor(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Armor() {
    }

    public Armor(Long id, String name, String description, String image, short ac, String type, Double weight, Integer price) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.image = image;
        this.ac = ac;
        this.type = type;
        this.weight = weight;
        this.price = price;
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

    public short getAC() {
        return ac;
    }

    public void setAC(short ac) {
        this.ac = ac;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || id == null) return false;
        Armor armor = (Armor) o;
        return Objects.equals(id, armor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}