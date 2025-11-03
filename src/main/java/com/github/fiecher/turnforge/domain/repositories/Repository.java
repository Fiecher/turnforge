package com.github.fiecher.turnforge.domain.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {

    ID save(T entity);

    Optional<T> findByID(ID entityID);

    List<T> findAll();

    T update(T entity);

    void deleteByID(ID entityID);
}
