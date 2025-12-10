package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends Repository<Skill, Long> {

    Optional<Skill> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);

    List<Skill> findAllByCharacterID(Long characterID);
}