package com.github.fiecher.turnforge.domain.services;

import com.github.fiecher.turnforge.domain.models.Skill;
import com.github.fiecher.turnforge.domain.repositories.SkillRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = Objects.requireNonNull(skillRepository);
    }

    public Long createSkill(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Skill name cannot be empty.");

        if (skillRepository.existsByName(name)) {
            throw new IllegalStateException("Skill with name " + name + " already exists.");
        }

        Skill skill = new Skill(name);
        return skillRepository.save(skill);
    }

    public Optional<Skill> getSkillById(Long skillID) {
        if (skillID == null) return Optional.empty();
        return skillRepository.findByID(skillID);
    }

    public Optional<Skill> getSkillByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return skillRepository.findByName(name);
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill updateSkill(Skill skill) {
        Objects.requireNonNull(skill, "Skill object cannot be null.");
        if (skill.getID() == null) throw new IllegalArgumentException("Skill ID must be set for update.");

        Optional<Skill> existingSkill = skillRepository.findByName(skill.getName());
        if (existingSkill.isPresent() && !Objects.equals(existingSkill.get().getID(), skill.getID())) {
            throw new IllegalStateException("Another skill with name " + skill.getName() + " already exists.");
        }

        return skillRepository.update(skill);
    }

    public void deleteSkillByID(Long skillId) {
        if (skillId == null) return;
        skillRepository.deleteByID(skillId);
    }
}