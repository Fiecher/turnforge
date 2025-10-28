package com.github.fiecher.turnforge.presentation.cli.commands.character;

import com.github.fiecher.turnforge.app.dtos.CharacterDetails;
import com.github.fiecher.turnforge.app.dtos.GetCharactersRequest;
import com.github.fiecher.turnforge.app.dtos.GetCharactersResponse;
import com.github.fiecher.turnforge.app.usecase.GetCharactersUseCase;
import com.github.fiecher.turnforge.presentation.cli.ApplicationContext;
import com.github.fiecher.turnforge.presentation.cli.output.View;
import com.github.fiecher.turnforge.presentation.cli.commands.Command;
import com.github.fiecher.turnforge.domain.models.User;

import java.util.List;
import java.util.Objects;

public class GetCharactersCommand implements Command {

    private final GetCharactersUseCase getCharactersUseCase;
    private final ApplicationContext context;
    private final View view;

    public GetCharactersCommand(
            GetCharactersUseCase getCharactersUseCase,
            ApplicationContext context,
            View view) {

        this.getCharactersUseCase = Objects.requireNonNull(getCharactersUseCase);
        this.context = Objects.requireNonNull(context);
        this.view = Objects.requireNonNull(view);
    }

    @Override
    public String getName() {
        return "View My Characters";
    }

    @Override
    public void execute() {
        try {
            if (!context.isAuthenticated()){
                throw new IllegalStateException("Command can only be run by an authenticated user.");
            }

            User currentUser = context.getCurrentUser();
            Long userID = currentUser.getID();

            view.showMessage("\n --- Viewing Characters for User ID: " + userID + " ---");

            GetCharactersResponse response = getCharactersUseCase.execute(
                    new GetCharactersRequest(userID)
            );

            List<CharacterDetails> characters = response.characters();

            if (characters.isEmpty()) {
                view.showMessage("You have no characters yet. Use `Create new Character` command.");
            } else {
                view.showMessage("Found " + characters.size() + " characters:");
                characters.forEach(c ->
                        view.showMessage("  ID: " + c.getID() + ", Name: " + c.getName() + ", Class: " + c.getCharacterClass())
                );
            }

        } catch (Exception e) {
            view.showError("Failed to retrieve characters: " + e.getMessage());
        }
    }
}