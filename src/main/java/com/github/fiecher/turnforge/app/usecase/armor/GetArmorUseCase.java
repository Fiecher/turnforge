package com.github.fiecher.turnforge.app.usecase.armor;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Armor;
import com.github.fiecher.turnforge.domain.services.ArmorService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GetArmorUseCase implements UseCase<Long, Optional<List<Armor>>> {
    private final ArmorService service;
    public GetArmorUseCase(ArmorService service) { this.service = Objects.requireNonNull(service); }

    @Override
    public Optional<List<Armor>> execute(Long id) {
        if (id == null) return Optional.of(service.getAllArmor());
        return service.getArmorByID(id).map(List::of);
    }
}
