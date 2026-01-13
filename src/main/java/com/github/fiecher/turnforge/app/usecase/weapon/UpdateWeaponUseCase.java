package com.github.fiecher.turnforge.app.usecase.weapon;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateWeaponRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Weapon;
import com.github.fiecher.turnforge.domain.services.WeaponService;
import java.util.Objects;

public class UpdateWeaponUseCase implements UseCase<UpdateWeaponRequest, Weapon> {
    private final WeaponService service;

    public UpdateWeaponUseCase(WeaponService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Weapon execute(UpdateWeaponRequest input) {
        if (input.id() == null) {
            throw new IllegalArgumentException("Weapon ID must be provided for update.");
        }

        Weapon weapon = new Weapon(
                input.id(),
                input.name(),
                input.description(),
                input.image(),
                input.damage(),
                input.type(),
                input.properties(),
                input.weight(),
                input.price()
        );
        return service.updateWeapon(weapon);
    }
}