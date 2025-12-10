package com.github.fiecher.turnforge.app.usecase.item;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Item;
import com.github.fiecher.turnforge.domain.services.ItemService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GetItemUseCase implements UseCase<Long, Optional<List<Item>>> {
    private final ItemService service;
    public GetItemUseCase(ItemService service) { this.service = Objects.requireNonNull(service); }

    @Override
    public Optional<List<Item>> execute(Long id) {
        if (id == null) return Optional.of(service.getAllItems());
        return service.getItemById(id).map(List::of);
    }
}
