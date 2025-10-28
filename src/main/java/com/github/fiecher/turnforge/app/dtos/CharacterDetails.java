package com.github.fiecher.turnforge.app.dtos;

import java.util.List;
import java.util.Collections;

public class CharacterDetails {
    private Long id;
    private String name;
    private String characterClass;
    private Short level;
    private Short strength;
    private Short dexterity;
    private Short constitution;
    private Short intelligence;
    private Short wisdom;
    private Short charisma;
    private String description;
    private String image;
    private String race;

    private List<String> abilities;
    private List<String> skills;
    private List<String> weapons;
    private List<String> armor;
    private List<String> items;

    public CharacterDetails() {
        this.abilities = Collections.emptyList();
        this.skills = Collections.emptyList();
        this.weapons = Collections.emptyList();
        this.armor = Collections.emptyList();
        this.items = Collections.emptyList();
    }

    public CharacterDetails(Long id, String name, String characterClass, Short level, Short strength, Short dexterity, Short constitution, Short intelligence, Short wisdom, Short charisma, String description, String image, String race, List<String> abilities, List<String> skills, List<String> weapons, List<String> armor, List<String> items) {
        this.id = id;
        this.name = name;
        this.characterClass = characterClass;
        this.level = level;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.description = description;
        this.image = image;
        this.race = race;
        this.abilities = abilities;
        this.skills = skills;
        this.weapons = weapons;
        this.armor = armor;
        this.items = items;
    }

    public Long getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public Short getLevel() {
        return level;
    }

    public Short getStrength() {
        return strength;
    }

    public Short getDexterity() {
        return dexterity;
    }

    public Short getConstitution() {
        return constitution;
    }

    public Short getIntelligence() {
        return intelligence;
    }

    public Short getWisdom() {
        return wisdom;
    }

    public Short getCharisma() {
        return charisma;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getRace() {
        return race;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getWeapons() {
        return weapons;
    }

    public List<String> getArmor() {
        return armor;
    }

    public List<String> getItems() {
        return items;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public void setStrength(Short strength) {
        this.strength = strength;
    }

    public void setDexterity(Short dexterity) {
        this.dexterity = dexterity;
    }

    public void setConstitution(Short constitution) {
        this.constitution = constitution;
    }

    public void setIntelligence(Short intelligence) {
        this.intelligence = intelligence;
    }

    public void setWisdom(Short wisdom) {
        this.wisdom = wisdom;
    }

    public void setCharisma(Short charisma) {
        this.charisma = charisma;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setWeapons(List<String> weapons) {
        this.weapons = weapons;
    }

    public void setArmor(List<String> armor) {
        this.armor = armor;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}