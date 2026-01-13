package com.github.fiecher.turnforge.app.usecase.item;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateItemRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Item;
import com.github.fiecher.turnforge.domain.services.ItemService;
import java.util.Objects;

public class UpdateItemUseCase implements UseCase<UpdateItemRequest, Item> {
    private final ItemService service;

    public UpdateItemUseCase(ItemService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Item execute(UpdateItemRequest input) {
        if (input.id() == null) {
            throw new IllegalArgumentException("Item ID must be provided for update.");
        }

        Item item = new Item(
                input.id(),
                input.name(),
                input.description(),
                input.image(),
                input.weight(),
                input.price()
        );
        return service.updateItem(item);
    }
}