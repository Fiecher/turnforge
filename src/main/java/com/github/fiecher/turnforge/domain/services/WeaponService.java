package com.github.fiecher.turnforge.domain.services;

import com.github.fiecher.turnforge.domain.models.Weapon;
import com.github.fiecher.turnforge.domain.repositories.WeaponRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WeaponService {

    private final WeaponRepository weaponRepository;

    public WeaponService(WeaponRepository weaponRepository) {
        this.weaponRepository = Objects.requireNonNull(weaponRepository);
    }

    public Long createWeapon(String name, String damage) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Weapon name cannot be empty.");
        if (damage == null || damage.trim().isEmpty()) throw new IllegalArgumentException("Weapon damage cannot be empty.");

        if (weaponRepository.existsByName(name)) {
            throw new IllegalStateException("Weapon with name " + name + " already exists.");
        }

        Weapon weapon = new Weapon(name, damage);
        return weaponRepository.save(weapon);
    }

    public Optional<Weapon> getWeaponByID(Long weaponID) {
        if (weaponID == null) return Optional.empty();
        return weaponRepository.findByID(weaponID);
    }

    public Optional<Weapon> getWeaponByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return weaponRepository.findByName(name);
    }

    public List<Weapon> getAllWeapons() {
        return weaponRepository.findAll();
    }

    public Weapon updateWeapon(Weapon weapon) {
        Objects.requireNonNull(weapon, "Weapon object cannot be null.");
        if (weapon.getID() == null) throw new IllegalArgumentException("Weapon ID must be set for update.");

        Optional<Weapon> existingWeapon = weaponRepository.findByName(weapon.getName());
        if (existingWeapon.isPresent() && !Objects.equals(existingWeapon.get().getID(), weapon.getID())) {
            throw new IllegalStateException("Another weapon with name " + weapon.getName() + " already exists.");
        }

        return weaponRepository.update(weapon);
    }

    public void deleteWeaponByID(Long weaponID) {
        if (weaponID == null) return;
        weaponRepository.deleteByID(weaponID);
    }
}