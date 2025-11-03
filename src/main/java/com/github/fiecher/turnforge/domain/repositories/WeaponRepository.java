package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Weapon;

import java.util.Optional;

public interface WeaponRepository extends Repository<Weapon, Long> {

    Optional<Weapon> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);
}