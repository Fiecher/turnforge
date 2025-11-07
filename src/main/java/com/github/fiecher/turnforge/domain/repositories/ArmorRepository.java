package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Armor;

import java.util.Optional;

public interface ArmorRepository extends Repository<Armor, Long>{

    Optional<Armor> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);
}