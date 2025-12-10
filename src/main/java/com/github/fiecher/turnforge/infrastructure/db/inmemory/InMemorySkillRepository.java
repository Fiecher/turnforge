package com.github.fiecher.turnforge.infrastructure.db.inmemory;

import com.github.fiecher.turnforge.domain.models.Skill;
import com.github.fiecher.turnforge.domain.repositories.SkillRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemorySkillRepository implements SkillRepository {

    private final Map<Long, Skill> skillsByID = new ConcurrentHashMap<>();
    private final Map<String, Skill> skillsByName = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Long save(Skill skill) {
        Objects.requireNonNull(skill);

        if (skill.getID() == null) {
            if (skillsByName.containsKey(skill.getName())) {
                throw new IllegalStateException("Skill with name '" + skill.getName() + "' already exists.");
            }
            skill.setID(idGenerator.getAndIncrement());
        } else {
            Skill existingByName = skillsByName.get(skill.getName());
            if (existingByName != null && !existingByName.getID().equals(skill.getID())) {
                throw new IllegalStateException("Cannot update skill; name is already used by ID: " + existingByName.getID());
            }

            Skill existingSkill = skillsByID.get(skill.getID());
            if (existingSkill != null && !existingSkill.getName().equals(skill.getName())) {
                skillsByName.remove(existingSkill.getName());
            }
        }

        skillsByID.put(skill.getID(), skill);
        skillsByName.put(skill.getName(), skill);

        return skill.getID();
    }

    @Override
    public Optional<Skill> findByID(Long skillId) {
        if (skillId == null) return Optional.empty();
        return Optional.ofNullable(skillsByID.get(skillId));
    }

    @Override
    public Optional<Skill> findByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return Optional.ofNullable(skillsByName.get(name));
    }

    @Override
    public List<Skill> findAll() {
        return new ArrayList<>(skillsByID.values());
    }

    @Override
    public Skill update(Skill skill) {
        if (skill.getID() == null || !skillsByID.containsKey(skill.getID())) {
            throw new IllegalArgumentException("Skill ID must be present and exist for update.");
        }
        this.save(skill);
        return skill;
    }

    @Override
    public void deleteByID(Long skillId) {
        if (skillId == null) return;
        Skill skill = skillsByID.remove(skillId);
        if (skill != null) {
            skillsByName.remove(skill.getName());
        }
    }

    @Override
    public void deleteByName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        Skill skill = skillsByName.remove(name);
        if (skill != null && skill.getID() != null) {
            skillsByID.remove(skill.getID());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return skillsByName.containsKey(name);
    }

    @Override
    public List<Skill> findAllByCharacterID(Long characterID) {
        return List.of();
    }
}