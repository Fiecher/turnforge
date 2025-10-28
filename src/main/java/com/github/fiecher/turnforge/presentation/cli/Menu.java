package com.github.fiecher.turnforge.presentation.cli;

import com.github.fiecher.turnforge.presentation.cli.commands.Command;
import com.github.fiecher.turnforge.presentation.cli.input.InputReader;
import com.github.fiecher.turnforge.presentation.cli.output.View;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Menu {
    private final View view;
    private final InputReader reader;
    private final ApplicationContext context;


    private final Map<Integer, Command> publicCommands;
    private final Map<Integer, Command> authenticatedCommands;

    public Menu(View view, InputReader reader, ApplicationContext context) {
        this.view = Objects.requireNonNull(view);
        this.reader = Objects.requireNonNull(reader);
        this.context = Objects.requireNonNull(context);
        this.publicCommands = new LinkedHashMap<>();
        this.authenticatedCommands = new LinkedHashMap<>();
    }

    public void registerCommand(Command command, boolean isPublic) {
        Map<Integer, Command> targetMap = isPublic ? publicCommands : authenticatedCommands;
        int commandNumber = targetMap.size() + 1;
        targetMap.put(commandNumber, command);
    }

    private Map<Integer, Command> showMenu(boolean isPublicMenu) {
        Map<Integer, Command> currentCommands = isPublicMenu ? publicCommands : authenticatedCommands;

        view.showMessage(isPublicMenu ? "\n--- Main Menu ---" : "\n--- User Menu: " + context.getCurrentUser().getLogin() + " ---");

        currentCommands.forEach((number, command) -> view.showMessage(number + ". " + command.getName()));

        view.showMessage(isPublicMenu ? "0. Exit" : "0. Logout");

        return currentCommands;
    }

    public void start() {
        view.showWelcome();

        boolean running = true;
        while (running) {
            boolean isAuthenticated = context.isAuthenticated();

            Map<Integer, Command> currentCommands = showMenu(!isAuthenticated);

            int choice = reader.readInt("Enter choice: ");
            if (isAuthenticated) {
                if (choice == 0) {
                    logout();
                } else if (currentCommands.containsKey(choice)) {
                    currentCommands.get(choice).execute();
                } else {
                    view.showError("Invalid choice. Please try again.");
                }
                view.showDivider();
            } else {
                if (choice == 0) {
                    exit();
                    running = false;
                } else if (currentCommands.containsKey(choice)) {
                    currentCommands.get(choice).execute();
                } else {
                    view.showError("Invalid choice. Please try again.");
                }
                view.showDivider();
            }
        }
        reader.close();
    }

    private void logout() {
        context.logout();
        view.showSuccess("Logged out successfully!");
    }

    private void exit() {
        view.showMessage("Goodbye!");
    }
}

