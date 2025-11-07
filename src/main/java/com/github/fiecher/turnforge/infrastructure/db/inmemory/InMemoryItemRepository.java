package com.github.fiecher.turnforge.infrastructure.db.inmemory;

import com.github.fiecher.turnforge.domain.models.Item;
import com.github.fiecher.turnforge.domain.repositories.ItemRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> itemsByID = new ConcurrentHashMap<>();
    private final Map<String, Item> itemsByName = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Long save(Item item) {
        Objects.requireNonNull(item);

        if (item.getID() == null) {
            if (itemsByName.containsKey(item.getName())) {
                throw new IllegalStateException("Item with name '" + item.getName() + "' already exists.");
            }
            item.setID(idGenerator.getAndIncrement());
        } else {
            Item existingByName = itemsByName.get(item.getName());
            if (existingByName != null && !existingByName.getID().equals(item.getID())) {
                throw new IllegalStateException("Cannot update item; name is already used by ID: " + existingByName.getID());
            }

            Item existingItem = itemsByID.get(item.getID());
            if (existingItem != null && !existingItem.getName().equals(item.getName())) {
                itemsByName.remove(existingItem.getName());
            }
        }

        itemsByID.put(item.getID(), item);
        itemsByName.put(item.getName(), item);

        return item.getID();
    }

    @Override
    public Optional<Item> findByID(Long itemId) {
        if (itemId == null) return Optional.empty();
        return Optional.ofNullable(itemsByID.get(itemId));
    }

    @Override
    public Optional<Item> findByName(String name) {
        if (name == null || name.trim().isEmpty()) return Optional.empty();
        return Optional.ofNullable(itemsByName.get(name));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(itemsByID.values());
    }

    @Override
    public Item update(Item item) {
        if (item.getID() == null || !itemsByID.containsKey(item.getID())) {
            throw new IllegalArgumentException("Item ID must be present and exist for update.");
        }
        this.save(item);
        return item;
    }

    @Override
    public void deleteByID(Long itemId) {
        if (itemId == null) return;
        Item item = itemsByID.remove(itemId);
        if (item != null) {
            itemsByName.remove(item.getName());
        }
    }

    @Override
    public void deleteByName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        Item item = itemsByName.remove(name);
        if (item != null && item.getID() != null) {
            itemsByID.remove(item.getID());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return itemsByName.containsKey(name);
    }
}