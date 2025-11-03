package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Item;

import java.util.Optional;

public interface ItemRepository extends Repository<Item, Long> {

    Optional<Item> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);
}