package com.github.fiecher.turnforge.app.usecase.trait;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Trait;
import com.github.fiecher.turnforge.domain.services.TraitService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GetTraitUseCase implements UseCase<Long, Optional<List<Trait>>> {
    private final TraitService service;

    public GetTraitUseCase(TraitService sevice) {
        this.service = Objects.requireNonNull(sevice);
    }

    @Override
    public Optional<List<Trait>> execute(Long id) {
        if (id == null) return Optional.of(service.getAllTraits());
        return service.getTraitByID(id).map(List::of);
    }


}
