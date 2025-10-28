package com.github.fiecher.turnforge.infrastructure.db.inmemory;

import com.github.fiecher.turnforge.domain.models.Trait;
import com.github.fiecher.turnforge.domain.repositories.TraitRepository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTraitRepository implements TraitRepository {

    private final Map<Long, Trait> traitsByID = new ConcurrentHashMap<>();
    private final Map<String, Trait> traitsByName = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Long save(Trait trait) {
        Objects.requireNonNull(trait);

        if (trait.getID() == null) {
            if (traitsByName.containsKey(trait.getName())) {
                throw new IllegalStateException("Trait with name '" + trait.getName() + "' already exists.");
            }
            trait.setID(idGenerator.getAndIncrement());
        } else {
            Trait existingByName = traitsByName.get(trait.getName());
            if (existingByName != null && !existingByName.getID().equals(trait.getID())) {
                throw new IllegalStateException("Cannot update trait; name is already used by ID: " + existingByName.getID());
            }

            Trait existingTrait = traitsByID.get(trait.getID());
            if (existingTrait != null && !existingTrait.getName().equals(trait.getName())) {
                traitsByName.remove(existingTrait.getName());
            }
        }

        traitsByID.put(trait.getID(), trait);
        traitsByName.put(trait.getName(), trait);

        return trait.getID();
    }

    @Override
    public Optional<Trait> findByID(Long traitId) {
        if (traitId == null) return Optional.empty();
        return Optional.ofNullable(traitsByID.get(traitId));
    }

    @Override
    public Optional<Trait> findByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return Optional.ofNullable(traitsByName.get(name));
    }

    @Override
    public List<Trait> findAll() {
        return new ArrayList<>(traitsByID.values());
    }

    @Override
    public Trait update(Trait trait) {
        if (trait.getID() == null || !traitsByID.containsKey(trait.getID())) {
            throw new IllegalArgumentException("Trait ID must be present and exist for update.");
        }
        this.save(trait);
        return trait;
    }

    @Override
    public void deleteByID(Long traitId) {
        if (traitId == null) return;
        Trait trait = traitsByID.remove(traitId);
        if (trait != null) {
            traitsByName.remove(trait.getName());
        }
    }

    @Override
    public void deleteByName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        Trait trait = traitsByName.remove(name);
        if (trait != null && trait.getID() != null) {
            traitsByID.remove(trait.getID());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return traitsByName.containsKey(name);
    }
}