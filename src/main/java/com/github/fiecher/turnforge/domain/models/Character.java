package com.github.fiecher.turnforge.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Character {
    private Long id;
    private final Long userID;
    private final String name;
    private short level;
    private short strength;
    private short dexterity;
    private short constitution;
    private short intelligence;
    private short wisdom;
    private short charisma;
    private String description;
    private String image;
    private final String class_;
    private String subclass;
    private String background;
    private String race;
    private int age;
    private SizeType size;
    private String spellcastingAbility;
    private int money;

    private final List<Long> skillIDs = new ArrayList<>();
    private final List<Long> traitIDs = new ArrayList<>();
    private final List<Long> abilityIDs = new ArrayList<>();
    private final List<Long> weaponIDs = new ArrayList<>();
    private final List<Long> itemIDs = new ArrayList<>();
    private final List<Long> armorIDs = new ArrayList<>();

    public Character(Long userID, String name, String class_) {
        if (userID == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (class_ == null || class_.trim().isEmpty()) {
            throw new IllegalArgumentException("Class cannot be empty");
        }
        this.userID = userID;
        this.name = name;
        this.class_ = class_;
        this.strength = 0;
        this.dexterity = 0;
        this.constitution = 0;
        this.intelligence = 0;
        this.wisdom = 0;
        this.charisma = 0;
        this.level = 1;
        this.money = 0;
    }

    public Character(Long id, Long userId, String name, String class_, short level, short strength, short dexterity, short constitution, short intelligence, short wisdom, short charisma, String description, String image, String subclass, String background, String race, Integer age, SizeType size, String spellcastingAbility, int money, List<Long> skillIds, List<Long> traitIds, List<Long> abilityIds, List<Long> weaponIds, List<Long> itemIds, List<Long> armorIds) {        this(userId, name, class_);
        this.id = id;
        this.level = level;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.description = description;
        this.image = image;
        this.subclass = subclass;
        this.background = background;
        this.race = race;
        this.age = age;
        this.size = size;
        this.spellcastingAbility = spellcastingAbility;
        this.money = money;
        if (skillIds != null) this.skillIDs.addAll(skillIds);
        if (traitIds != null) this.traitIDs.addAll(traitIds);
        if (abilityIds != null) this.abilityIDs.addAll(abilityIds);
        if (weaponIds != null) this.weaponIDs.addAll(weaponIds);
        if (itemIds != null) this.itemIDs.addAll(itemIds);
        if (armorIds != null) this.armorIDs.addAll(armorIds);
    }

    public Long getID() {
        return id;
    }

    public void setID(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("ID cannot be null");
        }
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be positive");
        }
        this.level = level;
    }

    public short getStrength() {
        return strength;
    }

    public void setStrength(short strength) {
        this.strength = (short) Math.max(0, strength);
    }

    public short getDexterity() {
        return dexterity;
    }

    public void setDexterity(short dexterity) {
        this.dexterity = (short) Math.max(0, dexterity);
    }

    public short getConstitution() {
        return constitution;
    }

    public void setConstitution(short constitution) {
        this.constitution = (short) Math.max(0, constitution);
    }

    public short getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(short intelligence) {
        this.intelligence = (short) Math.max(0, intelligence);
    }

    public short getWisdom() {
        return wisdom;
    }

    public void setWisdom(short wisdom) {
        this.wisdom = (short) Math.max(0, wisdom);
    }

    public short getCharisma() {
        return charisma;
    }

    public void setCharisma(short charisma) {
        this.charisma = (short) Math.max(0, charisma);
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

    public String getClass_() {
        return class_;
    }

    public String getSubclass() {
        return subclass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age != null && age >= 0 ? age : null;
    }

    public SizeType getSize() {
        return size;
    }

    public void setSize(SizeType size) {
        this.size = size;
    }

    public String getSpellcastingAbility() {
        return spellcastingAbility;
    }

    public void setSpellcastingAbility(String spellcastingAbility) {
        this.spellcastingAbility = spellcastingAbility;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = Math.max(0, money);
    }

    public List<Long> getSkillIDs() {
        return skillIDs;
    }

    public void addSkillID(Long skillId) {
        if (skillId != null && !skillIDs.contains(skillId)) {
            skillIDs.add(skillId);
        }
    }

    public void removeSkillID(Long skillId) {
        skillIDs.remove(skillId);
    }

    public List<Long> getTraitIDs() {
        return traitIDs;
    }

    public void addTraitId(Long traitId) {
        if (traitId != null && !traitIDs.contains(traitId)) {
            traitIDs.add(traitId);
        }
    }

    public void removeTraitId(Long traitId) {
        traitIDs.remove(traitId);
    }

    public List<Long> getAbilityIDs() {
        return abilityIDs;
    }

    public void addAbilityID(Long abilityId) {
        if (abilityId != null && !abilityIDs.contains(abilityId)) {
            abilityIDs.add(abilityId);
        }
    }

    public void removeAbilityID(Long abilityId) {
        abilityIDs.remove(abilityId);
    }

    public List<Long> getWeaponIDs() {
        return weaponIDs;
    }

    public void addWeaponID(Long weaponId) {
        if (weaponId != null && !weaponIDs.contains(weaponId)) {
            weaponIDs.add(weaponId);
        }
    }

    public void removeWeaponID(Long weaponId) {
        weaponIDs.remove(weaponId);
    }

    public List<Long> getItemIDs() {
        return itemIDs;
    }

    public void addItemID(Long itemId) {
        if (itemId != null && !itemIDs.contains(itemId)) {
            itemIDs.add(itemId);
        }
    }

    public void removeItemID(Long itemId) {
        itemIDs.remove(itemId);
    }

    public List<Long> getArmorIDs() {
        return armorIDs;
    }

    public void addArmorID(Long armorId) {
        if (armorId != null && !armorIDs.contains(armorId)) {
            armorIDs.add(armorId);
        }
    }

    public void removeArmorID(Long armorId) {
        armorIDs.remove(armorId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Character that = (Character) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

