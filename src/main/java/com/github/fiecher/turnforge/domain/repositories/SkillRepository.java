package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepository {

    Long save(Skill skill);

    Optional<Skill> findByID(Long skillID);

    Optional<Skill> findByName(String name);

    List<Skill> findAll();

    Skill update(Skill skill);

    void deleteByID(Long skillID);

    void deleteByName(String name);

    boolean existsByName(String name);
}