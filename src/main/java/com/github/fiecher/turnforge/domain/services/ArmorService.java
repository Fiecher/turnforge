package com.github.fiecher.turnforge.domain.services;

import com.github.fiecher.turnforge.domain.models.Armor;
import com.github.fiecher.turnforge.domain.repositories.ArmorRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ArmorService {

    private final ArmorRepository armorRepository;

    public ArmorService(ArmorRepository armorRepository) {
        this.armorRepository = Objects.requireNonNull(armorRepository);
    }

    public Long createArmor(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Armor name cannot be empty.");

        if (armorRepository.existsByName(name)) {
            throw new IllegalStateException("Armor with name " + name + " already exists.");
        }

        Armor armor = new Armor(name);
        return armorRepository.save(armor);
    }

    public Optional<Armor> getArmorByID(Long armorID) {
        if (armorID == null) return Optional.empty();
        return armorRepository.findByID(armorID);
    }

    public Optional<Armor> getArmorByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return armorRepository.findByName(name);
    }

    public List<Armor> getAllArmor() {
        return armorRepository.findAll();
    }

    public Armor updateArmor(Armor armor) {
        Objects.requireNonNull(armor, "Armor object cannot be null.");
        if (armor.getID() == null) throw new IllegalArgumentException("Armor ID must be set for update.");

        Optional<Armor> existingArmor = armorRepository.findByName(armor.getName());
        if (existingArmor.isPresent() && !Objects.equals(existingArmor.get().getID(), armor.getID())) {
            throw new IllegalStateException("Another armor with name " + armor.getName() + " already exists.");
        }

        return armorRepository.update(armor);
    }

    public void deleteArmorById(Long armorID) {
        if (armorID == null) return;
        armorRepository.deleteByID(armorID);
    }
}