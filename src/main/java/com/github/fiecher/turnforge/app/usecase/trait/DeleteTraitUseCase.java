package com.github.fiecher.turnforge.app.usecase.trait;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.TraitService;

import java.util.Objects;

public class DeleteTraitUseCase implements UseCase<Long, Void> {
    private final TraitService service;
    public DeleteTraitUseCase(TraitService service) { this.service = Objects.requireNonNull(service); }
    @Override
    public Void execute(Long id) { service.deleteTraitByID(id); return null; }
}