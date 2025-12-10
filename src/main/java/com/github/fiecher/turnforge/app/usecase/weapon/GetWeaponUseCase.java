package com.github.fiecher.turnforge.app.usecase.weapon;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Weapon;
import com.github.fiecher.turnforge.domain.services.WeaponService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GetWeaponUseCase implements UseCase<Long, Optional<List<Weapon>>> {
    private final WeaponService service;
    public GetWeaponUseCase(WeaponService service) { this.service = Objects.requireNonNull(service); }

    @Override
    public Optional<List<Weapon>> execute(Long id) {
        if (id == null) return Optional.of(service.getAllWeapons());
        return service.getWeaponByID(id).map(List::of);
    }
}