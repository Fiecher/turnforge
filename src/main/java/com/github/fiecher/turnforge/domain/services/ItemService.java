package com.github.fiecher.turnforge.domain.services;

import com.github.fiecher.turnforge.domain.models.Item;
import com.github.fiecher.turnforge.domain.repositories.ItemRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = Objects.requireNonNull(itemRepository);
    }

    public Long createItem(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Item name must not be empty.");

        if (itemRepository.existsByName(name)) {
            throw new IllegalStateException("Item with name " + name + " already exists.");
        }

        Item item = new Item(name);
        return itemRepository.save(item);
    }

    public Optional<Item> getItemById(Long itemId) {
        if (itemId == null) return Optional.empty();
        return itemRepository.findByID(itemId);
    }

    public Optional<Item> getItemByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return itemRepository.findByName(name);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item updateItem(Item item) {
        Objects.requireNonNull(item, "Item object must not be null.");
        if (item.getID() == null) throw new IllegalArgumentException("Item ID must be set for update.");

        Optional<Item> existingItem = itemRepository.findByName(item.getName());
        if (existingItem.isPresent() && !Objects.equals(existingItem.get().getID(), item.getID())) {
            throw new IllegalStateException("Another item with name " + item.getName() + " already exists.");
        }

        return itemRepository.update(item);
    }

    public void deleteItemById(Long itemId) {
        if (itemId == null) return;
        itemRepository.deleteByID(itemId);
    }
}