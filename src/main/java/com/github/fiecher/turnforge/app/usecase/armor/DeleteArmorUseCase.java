package com.github.fiecher.turnforge.app.usecase.armor;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.ArmorService;

import java.util.Objects;

public class DeleteArmorUseCase implements UseCase<Long, Void> {
    private final ArmorService service;
    public DeleteArmorUseCase(ArmorService service) { this.service = Objects.requireNonNull(service); }
    @Override
    public Void execute(Long id) { service.deleteArmorById(id); return null; }
}
