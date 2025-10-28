package com.github.fiecher.turnforge.domain.services;

import com.github.fiecher.turnforge.domain.models.Ability;
import com.github.fiecher.turnforge.domain.repositories.AbilityRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbilityService {

    private final AbilityRepository abilityRepository;

    public AbilityService(AbilityRepository abilityRepository) {
        this.abilityRepository = Objects.requireNonNull(abilityRepository);
    }

    public Long createAbility(String name, String damage) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Ability name cannot be empty.");
        if (damage == null || damage.trim().isEmpty()) throw new IllegalArgumentException("Ability damage cannot be empty.");

        if (abilityRepository.existsByName(name)) {
            throw new IllegalStateException("Ability with name " + name + " already exists.");
        }

        Ability ability = new Ability(name, damage);
        return abilityRepository.save(ability);
    }

    public Optional<Ability> getAbilityByID(Long abilityID) {
        if (abilityID == null) return Optional.empty();
        return abilityRepository.findByID(abilityID);
    }

    public Optional<Ability> getAbilityByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return abilityRepository.findByName(name);
    }

    public List<Ability> getAllAbilities() {
        return abilityRepository.findAll();
    }

    public Ability updateAbility(Ability ability) {
        Objects.requireNonNull(ability, "Ability cannot be null.");
        if (ability.getID() == null) throw new IllegalArgumentException("Ability ID must be set for update.");

        Optional<Ability> existingAbility = abilityRepository.findByName(ability.getName());
        if (existingAbility.isPresent() && !Objects.equals(existingAbility.get().getID(), ability.getID())) {
            throw new IllegalStateException("Ability with name " + ability.getName() + " already exists.");
        }

        return abilityRepository.update(ability);
    }

    public void deleteAbilityByID(Long abilityID) {
        if (abilityID == null) return;
        abilityRepository.deleteByID(abilityID);
    }
}