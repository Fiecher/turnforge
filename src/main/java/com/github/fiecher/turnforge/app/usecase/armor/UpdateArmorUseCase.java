package com.github.fiecher.turnforge.app.usecase.armor;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateArmorRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Armor;
import com.github.fiecher.turnforge.domain.services.ArmorService;

import java.util.Objects;

public class UpdateArmorUseCase implements UseCase<UpdateArmorRequest, Armor> {
    private final ArmorService service;
    public UpdateArmorUseCase(ArmorService service) { this.service = Objects.requireNonNull(service); }

    @Override
    public Armor execute(UpdateArmorRequest input) {
        if (input.id() == null) throw new IllegalArgumentException("ID required for update");
        Armor armor = new Armor(input.id(), input.name(), input.description(), input.image(), input.AC(), input.type(), input.weight(), input.price());
        return service.updateArmor(armor);
    }
}