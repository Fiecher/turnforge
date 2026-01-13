package com.github.fiecher.turnforge.app.usecase.item;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.ItemService;

import java.util.Objects;

public class DeleteItemUseCase implements UseCase<Long, Void> {
    private final ItemService service;
    public DeleteItemUseCase(ItemService service) { this.service = Objects.requireNonNull(service); }
    @Override
    public Void execute(Long id) { service.deleteItemById(id); return null; }
}