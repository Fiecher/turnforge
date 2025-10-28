package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Long save(User user);

    Optional<User> findByID(Long userID);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    User update(User user);

    void deleteByID(Long userID);

    void deleteByLogin(String login);

    boolean existByLogin(String login);
}
