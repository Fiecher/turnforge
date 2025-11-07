package com.github.fiecher.turnforge.domain.models;

import java.util.Objects;

public class Skill {
    private Long id;
    private final String name;
    private String description;

    public Skill(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Skill(Long id, String name, String description) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || id == null) return false;
        Skill skill = (Skill) o;
        return Objects.equals(id, skill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}