package com.github.fiecher.turnforge.domain.models;

import java.util.Objects;

public class Ability {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String damage;
    private String type;
    private short level;
    private String time;
    private String range;
    private String components;
    private String duration;

    public Ability(String name, String damage) {
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.damage = damage;
        this.level = 0;
    }

    public Ability(Long id, String name, String description, String image, String damage, String type, short level, String time, String range, String components, String duration) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.image = image;
        this.damage = damage;
        this.type = type;
        if (level < 0) {
            throw new IllegalArgumentException("Level cannot be a negative number: " + level);
        }
        this.level = level;
        this.time = time;
        this.range = range;
        this.components = components;
        this.duration = duration;
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

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getComponents() {
        return components;
    }

    public void setComponents(String components) {
        this.components = components;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || id == null) return false;
        Ability ability = (Ability) o;
        return Objects.equals(id, ability.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}