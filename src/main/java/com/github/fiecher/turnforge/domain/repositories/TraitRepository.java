package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Trait;

import java.util.List;
import java.util.Optional;

public interface TraitRepository {

    Long save(Trait trait);

    Optional<Trait> findByID(Long traitID);

    Optional<Trait> findByName(String name);

    List<Trait> findAll();

    Trait update(Trait trait);

    void deleteByID(Long traitID);

    void deleteByName(String name);

    boolean existsByName(String name);
}