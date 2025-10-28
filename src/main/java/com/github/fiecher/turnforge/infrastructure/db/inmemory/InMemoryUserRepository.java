package com.github.fiecher.turnforge.infrastructure.db.inmemory;

import com.github.fiecher.turnforge.domain.models.User;
import com.github.fiecher.turnforge.domain.repositories.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> usersByID = new HashMap<>();
    private final Map<String, User> usersByLogin = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Long save(User user) {
        if (user.getID() == null) {
            user.setID(idGenerator.getAndIncrement());
        }

        usersByID.put(user.getID(), user);
        usersByLogin.put(user.getLogin(), user);

        return user.getID();
    }

    @Override
    public Optional<User> findByID(Long id) {
        return Optional.ofNullable(usersByID.get(id));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(usersByLogin.get(login));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersByID.values());
    }

    @Override
    public User update(User user) {
        if (user.getID() == null || !usersByID.containsKey(user.getID())) {
            throw new IllegalArgumentException("User ID must be present for update and must exist.");
        }

        usersByID.put(user.getID(), user);
        usersByLogin.put(user.getLogin(), user);
        return user;
    }

    @Override
    public void deleteByID(Long userID) {
        User user = usersByID.remove(userID);
        if (user != null) {
            usersByLogin.remove(user.getLogin());
        }
    }

    @Override
    public void deleteByLogin(String login) {
        User user = usersByLogin.remove(login);
        if (user != null) {
            usersByID.remove(user.getID());
        }
    }

    @Override
    public boolean existByLogin(String login) {
        return usersByLogin.containsKey(login);
    }
}