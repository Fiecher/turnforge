package com.github.fiecher.turnforge.presentation.cli.commands.user;

import com.github.fiecher.turnforge.app.dtos.requests.LoginRequest;
import com.github.fiecher.turnforge.app.dtos.responses.UserDetails;
import com.github.fiecher.turnforge.app.usecase.LoginUserUseCase;
import com.github.fiecher.turnforge.presentation.cli.ApplicationContext;
import com.github.fiecher.turnforge.presentation.cli.commands.Command;
import com.github.fiecher.turnforge.presentation.cli.input.InputReader;
import com.github.fiecher.turnforge.presentation.cli.output.View;

import java.util.Objects;
import java.util.Optional;

public class LoginUserCommand implements Command {
    private final LoginUserUseCase authenticateUserUseCase;
    private final ApplicationContext context;
    private final View view;
    private final InputReader reader;

    public LoginUserCommand(
            LoginUserUseCase authenticateUserUseCase,
            ApplicationContext context,
            View view,
            InputReader reader
    ) {
        this.authenticateUserUseCase = Objects.requireNonNull(authenticateUserUseCase);
        this.context = Objects.requireNonNull(context);
        this.view = Objects.requireNonNull(view);
        this.reader = Objects.requireNonNull(reader);
    }

    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public void execute() {
        try {
            view.showMessage("\n--- User Login ---");
            String login = reader.readLine("Enter login: ");
            String password = reader.readLine("Enter password: ");

            Optional<UserDetails> user = authenticateUserUseCase.execute(
                    new LoginRequest(login, password)
            );

            if (user.isPresent()) {
                UserDetails authenticatedUser = user.get();

                context.login(authenticatedUser);

                view.showSuccess("Login successful! Welcome, " + authenticatedUser.login() + ".");
            } else {
                view.showError("Login failed: Invalid login or password.");
            }

        } catch (Exception e) {
            view.showError("An error occurred during login: " + e.getMessage());
        }
    }
}
