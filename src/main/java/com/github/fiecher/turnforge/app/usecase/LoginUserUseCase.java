package com.github.fiecher.turnforge.app.usecase;

import com.github.fiecher.turnforge.app.dtos.LoginRequest;
import com.github.fiecher.turnforge.app.dtos.UserDetails;
import com.github.fiecher.turnforge.domain.services.UserService;

import java.util.Objects;
import java.util.Optional;

public class LoginUserUseCase {
    private final UserService userService;

    public LoginUserUseCase(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

    public Optional<UserDetails> execute(LoginRequest request) {
        return userService.authenticateUser(request.login(), request.password())
                .map(user -> new UserDetails(user.getID(), user.getLogin(), user.getRole()));
    }
}
