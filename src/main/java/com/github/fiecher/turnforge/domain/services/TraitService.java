package com.github.fiecher.turnforge.domain.services;

import com.github.fiecher.turnforge.domain.models.Trait;
import com.github.fiecher.turnforge.domain.repositories.TraitRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TraitService {

    private final TraitRepository traitRepository;

    public TraitService(TraitRepository traitRepository) {
        this.traitRepository = Objects.requireNonNull(traitRepository);
    }

    public Long createTrait(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Trait name cannot be empty.");

        if (traitRepository.existsByName(name)) {
            throw new IllegalStateException("Trait with name " + name + " already exists.");
        }

        Trait trait = new Trait(name);
        return traitRepository.save(trait);
    }

    public Optional<Trait> getTraitByID(Long traitID) {
        if (traitID == null) return Optional.empty();
        return traitRepository.findByID(traitID);
    }

    public Optional<Trait> getTraitByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return traitRepository.findByName(name);
    }

    public List<Trait> getAllTraits() {
        return traitRepository.findAll();
    }

    public Trait updateTrait(Trait trait) {
        Objects.requireNonNull(trait, "Trait object cannot be null.");
        if (trait.getID() == null) throw new IllegalArgumentException("Trait ID must be set for update.");

        Optional<Trait> existingTrait = traitRepository.findByName(trait.getName());
        if (existingTrait.isPresent() && !Objects.equals(existingTrait.get().getID(), trait.getID())) {
            throw new IllegalStateException("Another trait with name " + trait.getName() + " already exists.");
        }

        return traitRepository.update(trait);
    }

    public void deleteTraitByID(Long traitId) {
        if (traitId == null) return;
        traitRepository.deleteByID(traitId);
    }
}