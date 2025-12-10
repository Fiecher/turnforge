package com.github.fiecher.turnforge.presentation.servlet.api;

import com.github.fiecher.turnforge.app.dtos.requests.CreateCharacterRequest;
import com.github.fiecher.turnforge.app.dtos.requests.GetCharactersRequest;
import com.github.fiecher.turnforge.app.dtos.requests.UpdateCharacterRequest;
import com.github.fiecher.turnforge.app.dtos.responses.CharacterCreationResponse;
import com.github.fiecher.turnforge.app.dtos.responses.CharacterDetails;
import com.github.fiecher.turnforge.app.dtos.responses.GetCharactersResponse;
import com.github.fiecher.turnforge.app.dtos.responses.UpdateCharacterResponse;
import com.github.fiecher.turnforge.app.usecase.character.AddAbilityToCharacterUseCase;
import com.github.fiecher.turnforge.app.usecase.character.CreateCharacterUseCase;
import com.github.fiecher.turnforge.app.usecase.character.GetCharactersUseCase;
import com.github.fiecher.turnforge.app.usecase.character.UpdateCharacterUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class CharacterServlet extends BaseServlet {

    private final CreateCharacterUseCase createCharacterUseCase;
    private final GetCharactersUseCase getCharactersUseCase;
    private final UpdateCharacterUseCase updateCharacterUseCase;
    private final AddAbilityToCharacterUseCase addAbilityToCharacterUseCase;

    public CharacterServlet(
            CreateCharacterUseCase createCharacterUseCase,
            GetCharactersUseCase getCharactersUseCase,
            UpdateCharacterUseCase updateCharacterUseCase,
            AddAbilityToCharacterUseCase addAbilityToCharacterUseCase) {

        this.createCharacterUseCase = Objects.requireNonNull(createCharacterUseCase);
        this.getCharactersUseCase = Objects.requireNonNull(getCharactersUseCase);
        this.updateCharacterUseCase = Objects.requireNonNull(updateCharacterUseCase);
        this.addAbilityToCharacterUseCase = Objects.requireNonNull(addAbilityToCharacterUseCase);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Long authenticatedUserId = getAuthUserId(req);
            Long characterIdFromPath = extractIdFromPath(req.getPathInfo());

            if (characterIdFromPath != null) {
                handleGetSingleCharacter(resp, authenticatedUserId, characterIdFromPath);
            } else {
                handleGetCharacterList(resp, authenticatedUserId);
            }
        } catch (IllegalStateException e) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required.");
        } catch (Exception e) {
            System.err.println("GET Character Error: " + e.getMessage());
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    private void handleGetCharacterList(HttpServletResponse resp, Long userId) throws IOException {
        GetCharactersRequest request = new GetCharactersRequest(userId);
        GetCharactersResponse response = getCharactersUseCase.execute(request);
        sendJson(resp, response.characters());
    }

    private void handleGetSingleCharacter(HttpServletResponse resp, Long userId, Long characterId) throws IOException {
        GetCharactersRequest request = new GetCharactersRequest(userId);
        Optional<CharacterDetails> characterDetails = getCharactersUseCase.execute(request).characters().stream()
                .filter(c -> Objects.equals(c.getID(), characterId))
                .findFirst();

        if (characterDetails.isEmpty()) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Character not found or access denied.");
            return;
        }
        sendJson(resp, characterDetails.get());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Long authenticatedUserId = getAuthUserId(req);

            CreateCharacterRequest requestBody = readBody(req, CreateCharacterRequest.class);

            CreateCharacterRequest finalRequest = new CreateCharacterRequest(
                    authenticatedUserId,
                    requestBody.name(),
                    requestBody.characterClass(),
                    requestBody.level(),
                    requestBody.race(),
                    requestBody.age(),
                    requestBody.size(),
                    requestBody.subclass(),
                    requestBody.background(),
                    requestBody.description(),
                    requestBody.strength(),
                    requestBody.dexterity(),
                    requestBody.constitution(),
                    requestBody.intelligence(),
                    requestBody.wisdom(),
                    requestBody.charisma(),
                    requestBody.spellcasting_ability(),
                    requestBody.money(),
                    requestBody.weapons(),
                    requestBody.armor(),
                    requestBody.items(),
                    requestBody.traits(),
                    requestBody.abilities(),
                    requestBody.custom_skills()
            );

            CharacterCreationResponse response = createCharacterUseCase.execute(finalRequest);

            sendCreated(resp, response.characterID(), "Character created successfully");

        } catch (IOException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request format or missing data.");
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (SecurityException e) {
            sendError(resp, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            System.err.println("POST Character Error: " + e.getMessage());
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Long authenticatedUserId = getAuthUserId(req);
            Long characterId = extractIdFromPath(req.getPathInfo());

            if (characterId == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Character ID is required in the path for PUT operation.");
                return;
            }

            UpdateCharacterRequest requestBody = readBody(req, UpdateCharacterRequest.class);


            UpdateCharacterRequest finalRequest = new UpdateCharacterRequest(
                    characterId,
                    requestBody.name(),
                    requestBody.characterClass(),
                    requestBody.level(),
                    requestBody.race(),
                    requestBody.age(),
                    requestBody.size(),
                    requestBody.subclass(),
                    requestBody.background(),
                    requestBody.description(),
                    requestBody.strength(),
                    requestBody.dexterity(),
                    requestBody.constitution(),
                    requestBody.intelligence(),
                    requestBody.wisdom(),
                    requestBody.charisma(),
                    requestBody.spellcasting_ability(),
                    requestBody.money(),
                    requestBody.weapons(),
                    requestBody.armor(),
                    requestBody.items(),
                    requestBody.traits(),
                    requestBody.abilities(),
                    requestBody.custom_skills()
            );

            UpdateCharacterResponse response = updateCharacterUseCase.execute(finalRequest, authenticatedUserId);

            sendJson(resp, response);

        } catch (IOException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request format or missing data.");
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SecurityException e) {
            sendError(resp, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            System.err.println("PUT Character Error: " + e.getMessage());
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }
}