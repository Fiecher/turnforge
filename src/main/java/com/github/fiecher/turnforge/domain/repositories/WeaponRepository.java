package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Weapon;

import java.util.List;
import java.util.Optional;

public interface WeaponRepository {

    Long save(Weapon weapon);

    Optional<Weapon> findByID(Long weaponID);

    Optional<Weapon> findByName(String name);

    List<Weapon> findAll();

    Weapon update(Weapon weapon);

    void deleteByID(Long weaponID);

    void deleteByName(String name);

    boolean existsByName(String name);
}