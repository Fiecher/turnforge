package com.github.fiecher.turnforge.infrastructure.db.inmemory;

import com.github.fiecher.turnforge.domain.models.Weapon;
import com.github.fiecher.turnforge.domain.repositories.WeaponRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryWeaponRepository implements WeaponRepository {

    private final Map<Long, Weapon> weaponsByID = new ConcurrentHashMap<>();
    private final Map<String, Weapon> weaponsByName = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Long save(Weapon weapon) {
        Objects.requireNonNull(weapon);

        if (weapon.getID() == null) {
            if (weaponsByName.containsKey(weapon.getName())) {
                throw new IllegalStateException("Weapon with name '" + weapon.getName() + "' already exists.");
            }
            weapon.setID(idGenerator.getAndIncrement());
        } else {
            Weapon existingByName = weaponsByName.get(weapon.getName());
            if (existingByName != null && !existingByName.getID().equals(weapon.getID())) {
                throw new IllegalStateException("Cannot update weapon; name is already used by ID: " + existingByName.getID());
            }

            Weapon existingWeapon = weaponsByID.get(weapon.getID());
            if (existingWeapon != null && !existingWeapon.getName().equals(weapon.getName())) {
                weaponsByName.remove(existingWeapon.getName());
            }
        }

        weaponsByID.put(weapon.getID(), weapon);
        weaponsByName.put(weapon.getName(), weapon);

        return weapon.getID();
    }

    @Override
    public Optional<Weapon> findByID(Long weaponId) {
        if (weaponId == null) return Optional.empty();
        return Optional.ofNullable(weaponsByID.get(weaponId));
    }

    @Override
    public Optional<Weapon> findByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return Optional.ofNullable(weaponsByName.get(name));
    }

    @Override
    public List<Weapon> findAll() {
        return new ArrayList<>(weaponsByID.values());
    }

    @Override
    public Weapon update(Weapon weapon) {
        if (weapon.getID() == null || !weaponsByID.containsKey(weapon.getID())) {
            throw new IllegalArgumentException("Weapon ID must be present and exist for update.");
        }
        this.save(weapon);
        return weapon;
    }

    @Override
    public void deleteByID(Long weaponId) {
        if (weaponId == null) return;
        Weapon weapon = weaponsByID.remove(weaponId);
        if (weapon != null) {
            weaponsByName.remove(weapon.getName());
        }
    }

    @Override
    public void deleteByName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        Weapon weapon = weaponsByName.remove(name);
        if (weapon != null && weapon.getID() != null) {
            weaponsByID.remove(weapon.getID());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return weaponsByName.containsKey(name);
    }

    @Override
    public List<Weapon> findAllByCharacterID(Long characterID) {
        return List.of();
    }
}