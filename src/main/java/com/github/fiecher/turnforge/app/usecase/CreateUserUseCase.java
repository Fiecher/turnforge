package com.github.fiecher.turnforge.app.usecase;

import com.github.fiecher.turnforge.app.dtos.CreateUserRequest;
import com.github.fiecher.turnforge.domain.services.UserService;

public class CreateUserUseCase implements UseCase<CreateUserRequest, Long> {

    private final UserService userService;

    public CreateUserUseCase(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Long execute(CreateUserRequest input) {
        return userService.registerUser(input.login(), input.password());
    }

}