package com.github.fiecher.turnforge.domain.models;

import java.util.Objects;

public class Weapon {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String damage;
    private String type;
    private String properties;
    private Double weight;
    private Integer price;

    public Weapon(String name, String damage) {
        this.name = Objects.requireNonNull(name);
        this.damage = Objects.requireNonNull(damage);
    }

    public Weapon(Long id, String name, String description, String image, String damage, String type, String properties, Double weight, Integer price) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.image = image;
        this.damage = damage;
        this.type = type;
        this.properties = properties;
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

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
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
        Weapon weapon = (Weapon) o;
        return Objects.equals(id, weapon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}