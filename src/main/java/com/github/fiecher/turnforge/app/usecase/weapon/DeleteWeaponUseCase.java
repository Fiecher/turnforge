package com.github.fiecher.turnforge.app.usecase.weapon;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.WeaponService;
import java.util.Objects;

public class DeleteWeaponUseCase implements UseCase<Long, Void> {
    private final WeaponService service;
    public DeleteWeaponUseCase(WeaponService service) { this.service = Objects.requireNonNull(service); }
    @Override
    public Void execute(Long id) { service.deleteWeaponByID(id); return null; }
}