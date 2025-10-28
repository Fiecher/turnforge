package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Ability;

import java.util.List;
import java.util.Optional;

public interface AbilityRepository {

    Long save(Ability ability);

    Optional<Ability> findByID(Long abilityID);

    Optional<Ability> findByName(String name);

    List<Ability> findAll();

    Ability update(Ability ability);

    void deleteByID(Long abilityID);

    void deleteByName(String name);

    boolean existsByName(String name);
}