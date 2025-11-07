package com.github.fiecher.turnforge.presentation.cli;

import com.github.fiecher.turnforge.app.dtos.responses.UserDetails;

public class ApplicationContext {
    private UserDetails currentUser;

    public UserDetails getCurrentUser() {
        return currentUser;
    }

    public void login(UserDetails user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
