package com.github.fiecher.turnforge.app.usecase.item;

import com.github.fiecher.turnforge.app.dtos.requests.CreateItemRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.ItemService;
import java.util.Objects;

public class CreateItemUseCase implements UseCase<CreateItemRequest, Long> {
    private final ItemService service;

    public CreateItemUseCase(ItemService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Long execute(CreateItemRequest input) {
        return service.createItem(
                input.name()
        );
    }
}