package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Ability;

import java.util.List;
import java.util.Optional;

public interface AbilityRepository extends Repository<Ability, Long> {
    Optional<Ability> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);

    List<Ability> findAllByCharacterID(Long characterID);
}