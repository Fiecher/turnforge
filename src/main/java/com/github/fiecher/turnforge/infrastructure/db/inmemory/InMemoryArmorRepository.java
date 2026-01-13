package com.github.fiecher.turnforge.infrastructure.db.inmemory;

import com.github.fiecher.turnforge.domain.models.Armor;
import com.github.fiecher.turnforge.domain.repositories.ArmorRepository;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArmorRepository implements ArmorRepository {

    private final Map<Long, Armor> armorByID = new HashMap<>();

    private final Map<String, Armor> armorByName = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Long save(Armor armor) {
        Objects.requireNonNull(armor, "Armor cannot be null.");

        if (armor.getID() == null && armorByName.containsKey(armor.getName())) {
            throw new IllegalStateException("Armor with name '" + armor.getName() + "' already exists.");
        }

        if (armor.getID() == null) {
            armor.setID(idGenerator.getAndIncrement());
        } else {
            Armor existingByName = armorByName.get(armor.getName());
            if (existingByName != null && !existingByName.getID().equals(armor.getID())) {
                throw new IllegalStateException("Cannot update armor; name is already used by ID: " + existingByName.getID());
            }
        }

        Armor existingArmor = armorByID.get(armor.getID());
        if (existingArmor != null && !existingArmor.getName().equals(armor.getName())) {
            armorByName.remove(existingArmor.getName());
        }

        armorByID.put(armor.getID(), armor);
        armorByName.put(armor.getName(), armor);

        return armor.getID();
    }

    @Override
    public Optional<Armor> findByID(Long armorId) {
        if (armorId == null) return Optional.empty();
        return Optional.ofNullable(armorByID.get(armorId));
    }

    @Override
    public Optional<Armor> findByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return Optional.ofNullable(armorByName.get(name));
    }

    @Override
    public List<Armor> findAll() {
        return new ArrayList<>(armorByID.values());
    }

    @Override
    public Armor update(Armor armor) {
        if (armor.getID() == null || !armorByID.containsKey(armor.getID())) {
            throw new IllegalArgumentException("Armor ID must be present and exist for update.");
        }
        this.save(armor);
        return armor;
    }

    @Override
    public void deleteByID(Long armorId) {
        if (armorId == null) return;
        Armor armor = armorByID.remove(armorId);
        if (armor != null) {
            armorByName.remove(armor.getName());
        }
    }

    @Override
    public void deleteByName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        Armor armor = armorByName.remove(name);
        if (armor != null && armor.getID() != null) {
            armorByID.remove(armor.getID());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return armorByName.containsKey(name);
    }

    @Override
    public List<Armor> findAllByCharacterID(Long characterID) {
        return List.of();
    }
}