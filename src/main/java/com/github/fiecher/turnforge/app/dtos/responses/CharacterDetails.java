package com.github.fiecher.turnforge.app.dtos.responses;

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
    private String subclass;
    private String background;
    private Integer age;
    private String size;
    private String spellcasting_ability;
    private Integer money;

    private List<String> abilities;
    private List<String> skills;
    private List<String> weapons;
    private List<String> armor;
    private List<String> items;
    private List<String> traits;

    public CharacterDetails() {
        this.abilities = Collections.emptyList();
        this.skills = Collections.emptyList();
        this.weapons = Collections.emptyList();
        this.armor = Collections.emptyList();
        this.items = Collections.emptyList();
        this.traits = Collections.emptyList();
    }

    public CharacterDetails(Long id, String name, String characterClass, Short level, Short strength, Short dexterity, Short constitution, Short intelligence, Short wisdom, Short charisma,
                            String description, String image, String race,
                            String subclass, String background, Integer age, String size, String spellcasting_ability, Integer money,
                            List<String> abilities, List<String> skills, List<String> weapons, List<String> armor, List<String> items, List<String> traits) {
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
        this.subclass = subclass;
        this.background = background;
        this.age = age;
        this.size = size;
        this.spellcasting_ability = spellcasting_ability;
        this.money = money;
        this.abilities = abilities;
        this.skills = skills;
        this.weapons = weapons;
        this.armor = armor;
        this.items = items;
        this.traits = traits;
    }

    public Long getID() { return id; }
    public String getName() { return name; }
    public String getCharacterClass() { return characterClass; }
    public Short getLevel() { return level; }
    public Short getStrength() { return strength; }
    public Short getDexterity() { return dexterity; }
    public Short getConstitution() { return constitution; }
    public Short getIntelligence() { return intelligence; }
    public Short getWisdom() { return wisdom; }
    public Short getCharisma() { return charisma; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public String getRace() { return race; }

    public String getSubclass() { return subclass; }
    public String getBackground() { return background; }
    public Integer getAge() { return age; }
    public String getSize() { return size; }
    public String getSpellcasting_ability() { return spellcasting_ability; }
    public Integer getMoney() { return money; }

    public List<String> getAbilities() { return abilities; }
    public List<String> getSkills() { return skills; }
    public List<String> getWeapons() { return weapons; }
    public List<String> getArmor() { return armor; }
    public List<String> getItems() { return items; }
    public List<String> getTraits() { return traits; }

    public void setID(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCharacterClass(String characterClass) { this.characterClass = characterClass; }
    public void setLevel(Short level) { this.level = level; }
    public void setStrength(Short strength) { this.strength = strength; }
    public void setDexterity(Short dexterity) { this.dexterity = dexterity; }
    public void setConstitution(Short constitution) { this.constitution = constitution; }
    public void setIntelligence(Short intelligence) { this.intelligence = intelligence; }
    public void setWisdom(Short wisdom) { this.wisdom = wisdom; }
    public void setCharisma(Short charisma) { this.charisma = charisma; }
    public void setDescription(String description) { this.description = description; }
    public void setImage(String image) { this.image = image; }
    public void setRace(String race) { this.race = race; }

    public void setSubclass(String subclass) { this.subclass = subclass; }
    public void setBackground(String background) { this.background = background; }
    public void setAge(Integer age) { this.age = age; }
    public void setSize(String size) { this.size = size; }
    public void setSpellcasting_ability(String spellcasting_ability) { this.spellcasting_ability = spellcasting_ability; }
    public void setMoney(Integer money) { this.money = money; }

    public void setAbilities(List<String> abilities) { this.abilities = abilities; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public void setWeapons(List<String> weapons) { this.weapons = weapons; }
    public void setArmor(List<String> armor) { this.armor = armor; }
    public void setItems(List<String> items) { this.items = items; }
    public void setTraits(List<String> traits) { this.traits = traits; }
}