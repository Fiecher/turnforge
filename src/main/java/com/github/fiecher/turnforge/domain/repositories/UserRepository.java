package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long>{

    Optional<User> findByLogin(String login);

    void deleteByLogin(String login);

    boolean existByLogin(String login);
}
