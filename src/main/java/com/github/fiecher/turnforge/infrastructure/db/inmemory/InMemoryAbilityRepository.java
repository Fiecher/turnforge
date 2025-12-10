package com.github.fiecher.turnforge.infrastructure.db.inmemory;


import com.github.fiecher.turnforge.domain.models.Ability;
import com.github.fiecher.turnforge.domain.repositories.AbilityRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryAbilityRepository implements AbilityRepository {

    private final Map<Long, Ability> abilitiesByID = new HashMap<>();

    private final Map<String, Ability> abilitiesByName = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Long save(Ability ability) {
        Objects.requireNonNull(ability, "Ability cannot be null.");

        if (ability.getID() == null && abilitiesByName.containsKey(ability.getName())) {
            throw new IllegalStateException("Ability with name '" + ability.getName() + "' already exists.");
        }

        if (ability.getID() == null) {
            ability.setID(idGenerator.getAndIncrement());
        } else {
            Ability existingByName = abilitiesByName.get(ability.getName());
            if (existingByName != null && !existingByName.getID().equals(ability.getID())) {
                throw new IllegalStateException("Cannot update ability; name is already used by ID: " + existingByName.getID());
            }
        }

        abilitiesByID.put(ability.getID(), ability);
        abilitiesByName.put(ability.getName(), ability);

        return ability.getID();
    }

    @Override
    public Optional<Ability> findByID(Long abilityId) {
        if (abilityId == null) return Optional.empty();
        return Optional.ofNullable(abilitiesByID.get(abilityId));
    }

    @Override
    public Optional<Ability> findByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return Optional.ofNullable(abilitiesByName.get(name));
    }

    @Override
    public List<Ability> findAll() {
        return new ArrayList<>(abilitiesByID.values());
    }

    @Override
    public Ability update(Ability ability) {
        if (ability.getID() == null || !abilitiesByID.containsKey(ability.getID())) {
            throw new IllegalArgumentException("Ability ID must be not null.");
        }

        this.save(ability);
        return ability;
    }

    @Override
    public void deleteByID(Long abilityId) {
        if (abilityId == null) return;
        Ability ability = abilitiesByID.remove(abilityId);
        if (ability != null) {
            abilitiesByName.remove(ability.getName());
        }
    }

    @Override
    public void deleteByName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        Ability ability = abilitiesByName.remove(name);
        if (ability != null && ability.getID() != null) {
            abilitiesByID.remove(ability.getID());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return abilitiesByName.containsKey(name);
    }

    @Override
    public List<Ability> findAllByCharacterID(Long characterID) {
        return List.of();
    }
}