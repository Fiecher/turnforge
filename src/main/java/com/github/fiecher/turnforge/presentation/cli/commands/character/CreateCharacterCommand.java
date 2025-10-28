package com.github.fiecher.turnforge.presentation.cli.commands.character;

import com.github.fiecher.turnforge.app.dtos.CharacterCreationResponse;
import com.github.fiecher.turnforge.app.dtos.CreateCharacterRequest;
import com.github.fiecher.turnforge.app.usecase.CreateCharacterUseCase;
import com.github.fiecher.turnforge.presentation.cli.ApplicationContext;
import com.github.fiecher.turnforge.presentation.cli.input.InputReader;
import com.github.fiecher.turnforge.presentation.cli.output.View;
import com.github.fiecher.turnforge.presentation.cli.commands.Command;
import com.github.fiecher.turnforge.domain.models.User;

public class CreateCharacterCommand implements Command {
    private final CreateCharacterUseCase createCharacterUseCase;
    private final ApplicationContext context;
    private final View view;
    private final InputReader reader;

    public CreateCharacterCommand(CreateCharacterUseCase createCharacterUseCase, ApplicationContext context, View view, InputReader reader) {
        this.createCharacterUseCase = createCharacterUseCase;
        this.context = context;
        this.view = view;
        this.reader = reader;
    }

    @Override
    public String getName() {
        return "Create new Character";
    }

    @Override
    public void execute() {
        if (!context.isAuthenticated()){
            throw new IllegalStateException("Command can only be run by an authenticated user.");
        }
        User currentUser = context.getCurrentUser();
        Long userID = currentUser.getID();

        try {
            view.showMessage("\n --- Creating Character ---");
            String name = reader.readLine("Enter name: ");
            String characterClass = reader.readLine("Enter class: ");
            CharacterCreationResponse characterID = createCharacterUseCase.execute(
                    new CreateCharacterRequest(userID, name, characterClass)
            );

            view.showSuccess("Character '" + name + "' created successfully with ID: " + characterID.characterID());

        } catch (Exception e) {
            view.showError("Creating character failed:" + e.getMessage());
        }
    }
}
