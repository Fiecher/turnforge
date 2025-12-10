package com.github.fiecher.turnforge.app.usecase.weapon;

import com.github.fiecher.turnforge.app.dtos.requests.CreateWeaponRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.WeaponService;
import java.util.Objects;

public class CreateWeaponUseCase implements UseCase<CreateWeaponRequest, Long> {
    private final WeaponService service;
    public CreateWeaponUseCase(WeaponService service) { this.service = Objects.requireNonNull(service); }

    @Override
    public Long execute(CreateWeaponRequest input) {
        return service.createWeapon(input.name(), input.damage());
    }
}