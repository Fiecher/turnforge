package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Armor;

import java.util.List;
import java.util.Optional;

public interface ArmorRepository {

    Long save(Armor armor);

    Optional<Armor> findByID(Long armorID);

    Optional<Armor> findByName(String name);

    List<Armor> findAll();

    Armor update(Armor armor);

    void deleteByID(Long armorID);

    void deleteByName(String name);

    boolean existsByName(String name);
}