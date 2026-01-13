package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Trait;

import java.util.List;
import java.util.Optional;

public interface TraitRepository extends Repository<Trait, Long>{

    Optional<Trait> findByName(String name);
    
    void deleteByName(String name);

    boolean existsByName(String name);

    List<Trait> findAllByCharacterID(Long characterID);
}