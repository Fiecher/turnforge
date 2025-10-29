package com.github.fiecher.turnforge.domain.models;

import java.util.Objects;

public final class User {
    private Long id;
    private final String login;
    private String passwordHash;
    private UserRole role;

    public User(String login, String passwordHash) {
        this.login = Objects.requireNonNull(login);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.role = UserRole.USER;
    }

    public User(Long id, String login, String passwordHash, UserRole role) {
        this.id = id;
        this.login = Objects.requireNonNull(login);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.role = Objects.requireNonNull(role);
    }

    public Long getID() {
        return id;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || id == null) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}