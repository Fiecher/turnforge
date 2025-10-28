package com.github.fiecher.turnforge.domain.services;

import com.github.fiecher.turnforge.domain.models.User;
import com.github.fiecher.turnforge.domain.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long registerUser(String login, String password) {
        if (userRepository.existByLogin(login)) {
            throw new IllegalStateException("User with login " + login + " already exists");
        }

        String hashedPassword = PasswordGenerator.getInstance().hash(password);
        User user = new User(login, hashedPassword);

        return userRepository.save(user);
    }

    public Optional<User> authenticateUser(String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);

        if (user.isPresent()) {
            String storedHash = user.get().getPasswordHash();
            if (PasswordGenerator.getInstance().verify(password, storedHash)) {
                return user;
            }
        }

        return Optional.empty();
    }

    public Optional<User> findByID(Long id) {
        return userRepository.findByID(id);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void changePassword(Long userID, String oldPassword, String newPassword) {
        Optional<User> user = userRepository.findByID(userID);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User u = user.get();
        if (!PasswordGenerator.getInstance().verify(oldPassword, u.getPasswordHash())) {
            throw new IllegalStateException("Invalid old password");
        }

        String hashedPassword = PasswordGenerator.getInstance().hash(newPassword);
        u.setPasswordHash(hashedPassword);
        userRepository.save(u);
    }
}
